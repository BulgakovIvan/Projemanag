package com.example.projemanag.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import com.example.projemanag.R
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.utils.Constants.TAG

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        fullScreen()

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        findViewById<TextView>(R.id.tv_app_name).typeface = typeFace

        Looper.myLooper()?.let {
            Handler(it).postDelayed(
                {

                    val currentUserId = FirestoreClass().getCurrentUserId()
                    if (currentUserId.isNotEmpty()) {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        startActivity(Intent(this, IntroActivity::class.java))
                    }
                    finish()
                },
                2500
            )
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