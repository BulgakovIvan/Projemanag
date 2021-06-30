package com.example.projemanag

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
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

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT in 16..29) { // lower api
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else if (Build.VERSION.SDK_INT >= 30) {
            window.decorView.windowInsetsController!!.hide(
                android.view.WindowInsets.Type.statusBars()
            )
        }
    }
}