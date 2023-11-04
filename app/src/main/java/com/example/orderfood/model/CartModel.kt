package com.example.orderfood.model

data class CartModel(
    val userId: String? = null,
    var cartItems: MutableList<CartItem>? = null,
    var totalQuantity: Int? = 0,
    var totalAmount: Double? = 0.0
)

