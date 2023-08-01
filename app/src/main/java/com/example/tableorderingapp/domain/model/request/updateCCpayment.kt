package com.example.tableorderingapp.domain.model.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class updateCCpayment(
    val id: Int,
    val isPaid: Boolean
):Parcelable