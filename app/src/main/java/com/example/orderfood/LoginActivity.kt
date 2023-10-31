package com.example.orderfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orderfood.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private  val binding: ActivityLoginBinding by lazy {

        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ẩn Action Bar
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }
        binding.donothaveaccount.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java);
            startActivity(intent)
        }
    }
}