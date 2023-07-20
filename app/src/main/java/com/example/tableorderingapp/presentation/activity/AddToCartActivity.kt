package com.example.tableorderingapp.presentation.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityAddToCartBinding
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.util.GlobalVariable
import com.google.gson.Gson
import java.text.DecimalFormat

class AddToCartActivity : AppCompatActivity() {
    private var _binding: ActivityAddToCartBinding? = null
    private var newItemorder: Submenu?= null

    var originalAmountToInclude:Double=0.0
    var amount:Double=0.0
    var sum:Double=0.0
    var intValue:Int=0
    var tag: Int=0
    var ordername:String?=null
    var gson= Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)
        overridePendingTransition(R.anim.enter_vertical, R.anim.exit_vertical)
        _binding= ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        newItemorder=GlobalVariable.itemToaddToCart
        initView(GlobalVariable.itemToaddToCart!!)

    }
    val initView:(Submenu)->Unit={
        var sizee= GlobalVariable.orderdetail?.size
        tag= it.tag
        computeWindowSizeClasses()
        if(!it.imageURL.isNullOrEmpty()){
            Glide.with(this)
                .load(it.imageURL)
                .circleCrop()
                .centerCrop()
                .into(_binding!!.ivSubitemImageAdd )
        }
        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(it.price)
        var originalAmount=formattedNumber.toString().plus(" php")
        ordername= it.name
        _binding!!.tvSubmenuName.text=it.name.plus(" (${originalAmount})")
        originalAmountToInclude = it.price
        amount=it.price

        _binding!!.ivCartAdd.setOnClickListener {
            var qty= _binding!!.qtyCounter.text.toString()
            intValue = qty.toInt()
            intValue = intValue + 1
            sum=amount * intValue
            _binding!!.qtyCounter.text= intValue.toString()

            val decimalFormat = DecimalFormat("#.00")
            val formattedNumber = decimalFormat.format(sum)
            _binding!!.txtitemprice.text=formattedNumber.toString()
            _binding!!.btnAddToCheckout.isEnabled=true
        }
        _binding!!.ivCartMinus.setOnClickListener {
            if(intValue<=0){
                _binding!!.btnAddToCheckout.isEnabled=false
                return@setOnClickListener
            }
            intValue=intValue-1
            sum=amount * intValue
            val decimalFormat = DecimalFormat("#.00")
            val formattedNumber = decimalFormat.format(sum)
            _binding!!.qtyCounter.text= intValue.toString()
            _binding!!.txtitemprice.text=formattedNumber.toString()
        }

        _binding!!.btnAddToCheckout.setOnClickListener {
            if(intValue<=0){
                Toast.makeText(this,"Please add quantity", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                var orderdetail= Orderdetail(
                    id = 0,
                    orderTitle = ordername.toString(),
                    orderTableTag = tag,
                    price = sum,
                    qty = intValue,
                    tableOrderId = 0,
                    originalPrice = originalAmountToInclude
                )
                GlobalVariable.orderdetail!!.add(orderdetail)
                val item = gson.toJson(GlobalVariable.orderdetail)
                Log.d("order",item)
               // Toast.makeText(this,"Item added to order", Toast.LENGTH_LONG).show()
                finish()
            }catch (e:Exception){
                Toast.makeText(this,"Error on ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun computeWindowSizeClasses() {
        try {
            val layout: LinearLayout = findViewById(R.id.mainLinear)
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            var width = displayMetrics.widthPixels
            var height = displayMetrics.heightPixels
            val autoScreenSize= when{
                width > 900f -> {
                    val params: ViewGroup.LayoutParams = layout.layoutParams
                    params.width = 600
                    layout.layoutParams = params
                }
                else -> {}
            }
        }catch (e:java.lang.Exception){
            Log.d("ta",e.localizedMessage)
        }
    }
}