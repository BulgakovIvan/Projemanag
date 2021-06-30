package com.example.projemanag.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.projemanag.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        fullScreen()

        findViewById<Button>(R.id.btn_sign_up_intro).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<Button>(R.id.btn_sign_in_intro).setOnClickListener {
            startActivity(Intent(this, SingInActivity::class.java))
        }
    }
}