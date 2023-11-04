package com.example.orderfood.Fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.orderfood.databinding.FragmentAddfoodBinding
import com.example.orderfood.model.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddfoodFragment : Fragment() {
    private lateinit var binding: FragmentAddfoodBinding
    private val database = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private var foodImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddfoodBinding.inflate(layoutInflater, container, false)
        binding.buttonAddFood.setOnClickListener {
            val nameFood = binding.editTextFoodName.text.toString().trim()
            val priceFood = binding.editTextFoodPrice.text.toString().trim()
            val descriptionFood = binding.editTextDescription.text.toString().trim()
            if (nameFood.isBlank() || priceFood.isBlank() || descriptionFood.isBlank()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                addFood(nameFood, priceFood, descriptionFood)

            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        return binding.root
    }

    private fun addFood(nameFood: String, priceFood: String, descriptionFood: String) {

        if (foodImageUri != null) {

            val imageRef =
                FirebaseStorage.getInstance().reference.child("food_image_${nameFood}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val newFood =
                        FoodModel(nameFood, priceFood, descriptionFood, downloadUrl.toString())
                    database.collection("foods")
                        .add(newFood)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Food data saved to Firestore",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.editTextFoodName.text.clear()
                            binding.editTextFoodPrice.text.clear()
                            binding.editTextDescription.text.clear()
                            binding.selectedImage.setImageURI(null)
                            foodImageUri = null

                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed to save food data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        } else {
            Toast.makeText(requireContext(), "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }
}
