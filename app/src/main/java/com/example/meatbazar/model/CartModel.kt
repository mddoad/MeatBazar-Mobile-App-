package com.example.meatbazar.model

data class CartModel(
    val productId: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val productImage: String = "",
    val quantity: Int = 1
)