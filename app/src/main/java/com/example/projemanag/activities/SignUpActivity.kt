package com.example.projemanag.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.projemanag.databinding.ActivitySighUpBinding

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySighUpBinding

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
            Toast.makeText(this, "Register user", Toast.LENGTH_SHORT).show()
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