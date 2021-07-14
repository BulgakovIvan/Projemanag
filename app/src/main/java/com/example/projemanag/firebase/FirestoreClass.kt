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
                Log.e(TAG, document.toString())

                activity.boardDetails(document.toObject(Board::class.java)!!)
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
                Log.e(TAG, document.toString())
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
                Log.e(TAG, "Error wile update profile data.")
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
                Log.e("ups", "Register error! ", e)
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
}