package com.example.tableorderingapp.presentation.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tableorderingapp.R

open class BaseActivity: AppCompatActivity() {

 private var mProgressDialog: Dialog? = null

  fun showCustomProgressDialog() {
  mProgressDialog = Dialog(this)
  mProgressDialog?.let {
   it.setContentView(R.layout.dialog_custom_progress)
   it.show()
   }
 }
  fun hideProgressDialog() {
  mProgressDialog?.let {
   it.dismiss()
  }
 }
}