package com.example.orderfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Ẩn Action Bar
        supportActionBar?.hide()
        Handler().postDelayed({
            var intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}