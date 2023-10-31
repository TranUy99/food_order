package com.example.orderfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.orderfood.databinding.ActivitySignupBinding
import com.example.orderfood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String
    private val database = FirebaseFirestore.getInstance()
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signupbutton.setOnClickListener {
            email = binding.editTextTextEmailAddress2.text.toString().trim()
            password = binding.editTextTextPassword2.text.toString().trim()
            name = binding.editTextTextPersonName.text.toString().trim()

            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password, name)
            }
        }

        binding.haveanyaccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                saveUserData()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        email = binding.editTextTextEmailAddress2.text.toString().trim()
        password = binding.editTextTextPassword2.text.toString().trim()
        name = binding.editTextTextPersonName.text.toString().trim()

        val user = UserModel(name, email, password)
        val userId = auth.currentUser?.uid
        if (userId != null) {
//            Log.d("UserModel", "Name: ${user.name}, Email: ${user.email}, Password: ${user.password}")
//            Log.d("UserID", userId)
            database.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "User data saved to Firestore", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }
}
