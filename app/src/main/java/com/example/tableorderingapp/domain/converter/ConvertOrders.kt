package com.example.tableorderingapp.domain.converter

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.request.PostTableOrder
import com.example.tableorderingapp.util.GlobalVariable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ConvertOrders {

    @RequiresApi(Build.VERSION_CODES.O)
    val createTableOrders:(List<Orderdetail>)-> PostTableOrder ={
        var methods:PostTableOrder?= null
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = currentDateTime.format(formatter)
        methods= PostTableOrder(
            dateTimeStamp =formattedDateTime,
            id=0,
            isPaid = false,
            orderPrice = computetotalOrder(GlobalVariable.orderdetail!!.toList()),
            orderStatus = "Pending",
            orderTagId = 0,
            orderdetails = GlobalVariable.orderdetail!!.toList(),
            tableNumber = GlobalVariable.tablenumber!!
        )
        methods
    }

    val computetotalOrder:(List<Orderdetail>) -> Double={
        var amount:Double=0.0
        it.forEach {
            amount+=it.price
        }
        amount
    }
}