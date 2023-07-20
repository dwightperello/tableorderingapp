package com.example.tableorderingapp.util

import android.content.Context
import android.content.SharedPreferences

object AppPreference {

    private const val NAME = "RestaurantClient"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    private val TABLE_NUMBER = Pair("tablenumber", "")

    fun init(context: Context){
        preferences=context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var tablenumber: String
        get() = preferences.getString(TABLE_NUMBER.first, TABLE_NUMBER.second) ?: "0"
        set(value) = preferences.edit {
            it.putString(TABLE_NUMBER.first, value)
        }
}