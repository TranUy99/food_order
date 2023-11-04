package com.example.foodordering.adaptar

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.databinding.PopularItemBinding
import com.example.orderfood.model.CartItem
import com.example.orderfood.model.CartModel
import com.example.orderfood.model.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class PopuplarAdapter(
    private val popularItems: List<FoodModel>,
    private val context: Context
) : RecyclerView.Adapter<PopuplarAdapter.PopuplarViewHolder>() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var userId: String // Để lưu trữ ID của người dùng hiện tại

    init {
        val user = auth.currentUser
        userId = user?.uid ?: ""

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopuplarViewHolder {

        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopuplarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopuplarViewHolder, position: Int) {
        val foodItem = popularItems[position]
        holder.bind(foodItem)
        holder.addItem(foodItem)
    }

    override fun getItemCount(): Int {
        return popularItems.size
    }

    inner class PopuplarViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: FoodModel) {
            binding.foodNamePopular.text = foodItem.foodName
            binding.PricePopular.text = foodItem.foodPrice
            Glide.with(context).load(Uri.parse(foodItem.foodImage)).into(binding.popuplarImage)
        }

        fun addItem(foodItem: FoodModel) {
            binding.addItemButton.setOnClickListener {
                if (foodItem.foodImage != null) {
                    val cartRef = database.collection("Cart").whereEqualTo("userId", userId)

                    cartRef.get().addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val cartDocument = querySnapshot.documents[0]
                            val cartData = cartDocument.toObject(CartModel::class.java)

                            if (cartData != null) {
                                val cartItems =
                                    cartData.cartItems?.toMutableList() ?: mutableListOf()
                                val existingCartItem =
                                    cartItems.find { it.foodName == foodItem.foodName }

                                if (existingCartItem != null) {
                                    existingCartItem.quantity = (existingCartItem.quantity ?: 0) + 1
                                } else {
                                    cartItems.add(
                                        CartItem(
                                            foodItem.id,
                                            foodItem.foodName,
                                            foodItem.foodPrice,
                                            foodItem.foodDescription,
                                            foodItem.foodImage,
                                            1
                                        )
                                    )
                                }

                                // Cập nhật danh sách cartItems trong CartModel
                                cartData.cartItems = cartItems

                                // Cập nhật tổng số tiền và tổng số lượng
                                cartData.totalQuantity = cartItems.sumBy { it.quantity ?: 0 }
                                cartData.totalAmount = cartItems.sumByDouble {
                                    (it.quantity ?: 0) * (it.foodPrice?.toDouble() ?: 0.0)
                                }

                                // Cập nhật lại tài liệu "Cart" trong Firestore
                                database.collection("Cart").document(cartDocument.id).set(cartData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Food added to the cart",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Failed to add food to the cart",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            val cartItems = mutableListOf(
                                CartItem(
                                    foodItem.id,
                                    foodItem.foodName,
                                    foodItem.foodPrice,
                                    foodItem.foodDescription,
                                    foodItem.foodImage,
                                    1
                                )
                            )

                            val newCart = CartModel(

                                userId,
                                cartItems,
                                cartItems.sumBy { it.quantity ?: 0 },
                                cartItems.sumByDouble {
                                    (it.quantity ?: 0) * (it.foodPrice?.toDouble() ?: 0.0)
                                }
                            )

                            database.collection("Cart")
                                .add(newCart)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Food added to the cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed to add food to the cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                } else {
                    Toast.makeText(context, "No image selected.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
