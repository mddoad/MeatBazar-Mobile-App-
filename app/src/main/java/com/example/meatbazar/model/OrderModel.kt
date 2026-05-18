package com.example.meatbazar.model

data class OrderModel(
    val orderId: String = "",
    val userId: String = "",
    val customerName: String = "",
    val address: String = "",
    val phone: String = "",
    val totalPrice: String = "",
    val orderStatus: String = "Pending"
)