package com.example.tableorderingapp.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CCpaymentOrderDetails(
    val creditcardorderdetails: List<Creditcardorderdetail>,
    val dateTimeStamp: String,
    val discountAmount: Double,
    val discountType: String,
    val id: Int,
    val isPaid: Boolean,
    val paymentType: String,
    val refnumber: Int,
    val serviceCharge: Double,
    val tableNumber: Int,
    val total: Double,
    val vat: Double,
    val vatAmount: Double
):Parcelable

@Parcelize
data class Creditcardorderdetail(
    val creditCardPaymentId: Int,
    val id: Int,
    val imageURL: String,
    val orderTableTag: Int,
    val orderTitle: String,
    val originalPrice: Double,
    val price: Double,
    val qty: Int
):Parcelable