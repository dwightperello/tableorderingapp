package com.example.tableorderingapp.util

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.tableorderingapp.R


@Suppress("DEPRECATION")
fun Toast.showCustomToast(message: String, activity: Activity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.toast_custom)
    )

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    this.apply {
        setGravity(Gravity.BOTTOM, 0, 150)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}