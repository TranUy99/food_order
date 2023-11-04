package com.example.foodordering.adaptar


import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.orderfood.databinding.CartItemBinding
import com.example.orderfood.model.CartItem
import com.example.orderfood.model.CartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CartAdapter(
    private val cartModel: CartModel,
    private val context: Context?
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val db = Firebase.firestore
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userId: String

    init {
        val user = auth.currentUser
        userId = user?.uid ?: ""

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartModel.cartItems?.size ?: 0

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            cartModel.cartItems?.let { cartItems ->
                if (position >= 0 && position < cartItems.size) {
                    val cartItem = cartItems[position]
                    binding.apply {
                        cartFoodName.text = cartItem.foodName
                        cartItemPrice.text = cartItem.foodPrice
                        Glide.with(context!!).load(Uri.parse(cartItem.foodImage)).into(cartImage)
                        cartQuantity.text = cartItem.quantity.toString()

                        minusButton.setOnClickListener {
                            if (cartItem.quantity!! > 1) {
                                cartItem.quantity = cartItem.quantity!! - 1
                                cartQuantity.text = cartItem.quantity.toString()

                                updateCartModelOnFirebase(cartModel)
                            }
                        }

                        plusButton.setOnClickListener {
                            cartItem.quantity = cartItem.quantity!! + 1
                            cartQuantity.text = cartItem.quantity.toString()
                            updateCartModelOnFirebase(cartModel)
                        }

                        deleteButton.setOnClickListener {
                            cartItems.removeAt(position)
                            updateCartModelOnFirebase(cartModel)
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        private fun updateCartModelOnFirebase(cartModel: CartModel) {
            val cartReference = db.collection("Cart")
            val query = cartReference.whereEqualTo("userId", userId)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val doc = documents.documents[0]

                        // Ghi đè toàn bộ giỏ hàng bằng giỏ hàng mới
                        doc.reference.set(cartModel)
                    }
                }
        }
    }
}

