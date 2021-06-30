package com.example.projemanag.activities

import android.os.Bundle
import com.example.projemanag.databinding.ActivitySingInBinding

class SingInActivity : BaseActivity() {
    private lateinit var binding: ActivitySingInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()
        setupActionBar(binding.toolbarSignUpActivity)
    }
}