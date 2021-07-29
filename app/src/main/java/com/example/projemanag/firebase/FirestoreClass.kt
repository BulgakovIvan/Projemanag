package com.example.projemanag.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projemanag.activities.*
import com.example.projemanag.models.Board
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.example.projemanag.utils.Constants.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                Log.e("ups", "Register error! ", e)
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String) {
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
//                Log.e(TAG, document.toString())

                val board = document.toObject(Board::class.java)!!
                board.documentId = documentId
                activity.boardDetails(board)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while download document")
            }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Board created successfully!", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating a board.", e)
            }
    }

    fun getBoardList(activity: MainActivity) {
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
//                Log.e(TAG, document.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document) {
                    val board = i.toObject(Board::class.java)
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardListToUI(boardList)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while download boardList")
            }
    }

    fun addUpdateTaskList(activity: BaseActivity, board: Board) {
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore
            .collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(TAG, "TaskList update successfully.")

                activity.addUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating a board.", e)
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.e(TAG, "Profile data updated successfully!")
                Toast.makeText(activity, "Profile data updated successfully!",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(TAG, "Error wile update profile data.", e)
                Toast.makeText(activity, "Error: profile data updated.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    fun loadUserData(activity: Activity, readBoardLids: Boolean = false) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null)

                    when (activity) {
                        is SingInActivity -> activity.singInSuccess(loggedInUser)
                        is MainActivity -> activity.updateNavigationUserDetails(loggedInUser, readBoardLids)
                        is MyProfileActivity -> activity.setUserDataInUI(loggedInUser)
                    }

            }.addOnFailureListener { e ->
                if (activity is BaseActivity) {
                    activity.hideProgressDialog()
                }
                Log.e(TAG, "Register error! ", e)
            }
    }

    fun getCurrentUserId(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }

        return currentUserId
    }

    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>) {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document ->
                Log.e(TAG, "Member list loaded.")

                val userList: ArrayList<User> = ArrayList()
                for (i in document.documents) {
                    val user = i.toObject(User::class.java)
                    if (user != null) userList.add(user)
                }
                if (activity is MembersActivity)
                    activity.setupMembersList(userList)
                else if (activity is TaskListActivity)
                    activity.boardMembersDetailsList(userList)
            }
            .addOnFailureListener { e ->
                if (activity is BaseActivity)
                    activity.hideProgressDialog()
                Log.e(TAG, "Error while loading a member list.", e)
            }
    }

    fun getMemberDetails(activity: MembersActivity, email: String) {
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener { document ->
                if (document.documents.size > 0) {
                    val user = document.documents[0].toObject(User::class.java)!!
                    activity.memberDetails(user)
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorShackBar("No such member found.")
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(TAG, "Error while getting user details.", e)
            }
    }

    fun assignedMemberToBoard(activity: MembersActivity, board: Board, user: User) {

        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToHashMap)
            .addOnSuccessListener { 
                activity.memberAssignSuccess(user)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating a board.", e)
            }
    }
}