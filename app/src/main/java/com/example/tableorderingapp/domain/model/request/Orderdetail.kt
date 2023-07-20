package com.example.tableorderingapp.domain.model.request

data class Orderdetail(
    val id: Int,
    val orderTableTag: Int,
    val orderTitle: String,
    val price: Double,
    val originalPrice: Double,
    val qty: Int,
    val tableOrderId: Int
)