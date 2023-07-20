package com.example.tableorderingapp.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllMenuModelItem(
    val description: String,
    val id: Int,
    val imageURL: String,
    val name: String,
    val submenu: List<Submenu>,
    val tag: Int
): Parcelable