package com.example.tableorderingapp.domain.model.request

data class PostTableOrder(
    val dateTimeStamp: String,
    val id: Int,
    val isPaid: Boolean,
    val orderPrice: Double,
    val orderStatus: String,
    val orderTagId: Int,
    var orderdetails: List<Orderdetail>,
    val tableNumber: Int
)