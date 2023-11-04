package com.example.orderfood.model

data class CartItem(
    val foodId :String? =null,
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    var quantity: Int? = 0
)

