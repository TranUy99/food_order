package com.example.orderfood

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orderfood.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private  val binding: ActivityStartBinding by lazy {

        ActivityStartBinding.inflate(layoutInflater)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Ẩn Action Bar
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.nextButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }

    }
}