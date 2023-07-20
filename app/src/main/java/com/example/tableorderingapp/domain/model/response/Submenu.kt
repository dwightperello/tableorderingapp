package com.example.tableorderingapp.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Submenu(
    val description: String,
    val id: Int,
    val imageURL: String,
    val isRecommended: Boolean,
    val menuId: Int,
    val name: String,
    val price: Double,
    val isAvailable:Boolean,
    val tag: Int
): Parcelable