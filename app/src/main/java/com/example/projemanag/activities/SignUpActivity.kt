package com.example.projemanag.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivitySighUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySighUpBinding
    val TAG = "ups"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySighUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()
        setupActionBar(binding.toolbarSignUpActivity)

        binding.btnSignUp.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val name: String = binding.etName.text.toString().trim { it <= ' '}
        val email: String = binding.etEmail.text.toString().trim { it <= ' '}
        val password: String = binding.etPassword.text.toString().trim { it <= ' '}

        if (validateForm(name, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        Toast.makeText(
                            this,
                            "$name you have successfully registered the email address $registeredEmail",
                            Toast.LENGTH_LONG
                        ).show()
                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorShackBar("Please enter a name")
                false
            }
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