package com.example.orderfood

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orderfood.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ẩn Action Bar
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // Người dùng đã đăng nhập, chuyển họ đến MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Người dùng chưa đăng nhập, hiển thị giao diện đăng nhập
            binding.nextButton.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
