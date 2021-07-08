package com.example.projemanag.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivitySingInBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class SingInActivity : BaseActivity() {
    private lateinit var binding: ActivitySingInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()
        setupActionBar(binding.toolbarSignInActivity)

        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener { singInUser() }
    }

    fun singInSuccess(user: User) {
        hideProgressDialog()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun singInUser() {
        val email: String = binding.etEmail.text.toString().trim{it <= ' '}
        val password: String = binding.etPassword.text.toString().trim{it <= ' '}

        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()

                    if (task.isSuccessful) {
                        Log.e(Constants.TAG, "createUserWithEmail: success")
                        FirestoreClass().loadUserData(this)

                    } else {
                        Log.e(Constants.TAG, "createUserWithEmail: failure", task.exception)
                        Toast.makeText(baseContext, task.exception!!.message,
                            Toast.LENGTH_LONG).show()
                    }
                }

        }
    }

    private fun validateForm(email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorShackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorShackBar("Please enter a password")
                false
            }
            else -> true
        }
    }
}