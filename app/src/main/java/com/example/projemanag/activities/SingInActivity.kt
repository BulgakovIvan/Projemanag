package com.example.projemanag.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivitySingInBinding
import com.google.firebase.auth.FirebaseAuth

class SingInActivity : BaseActivity() {
    private lateinit var binding: ActivitySingInBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "ups"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()
        setupActionBar(binding.toolbarSignInActivity)

        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener { singInUser() }
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
                        Log.e(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    } else {
                        Log.e(TAG, "createUserWithEmail:failure", task.exception)
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