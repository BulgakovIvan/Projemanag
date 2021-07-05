package com.example.projemanag.firebase

import android.util.Log
import com.example.projemanag.activities.SignUpActivity
import com.example.projemanag.activities.SingInActivity
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

    fun signInUser(activity: SingInActivity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null)
                    activity.singInSuccess(loggedInUser)

            }.addOnFailureListener { e ->
                Log.e("ups", "Register error! ", e)
            }
    }

    fun getCurrentUserId(): String {

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }

        return currentUserId
    }
}