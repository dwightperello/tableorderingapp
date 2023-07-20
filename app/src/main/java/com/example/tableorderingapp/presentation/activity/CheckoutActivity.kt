package com.example.tableorderingapp.presentation.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityCheckoutBinding
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.presentation.adapter.CheckoutAdapter

import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.GlobalVariable.orderdetail
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class CheckoutActivity : BaseActivity() {
    private lateinit var _binding: ActivityCheckoutBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var checkoutadapter: CheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        _binding= ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        init()
    }

    fun deleteItem(menus: Orderdetail) {
        try {
            GlobalVariable.orderdetail!!.remove(menus)

        }catch (e:Exception){
            Toast.makeText(this,"Error on ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        if(GlobalVariable.orderdetail!!.size>0){
            computeTotal(GlobalVariable.orderdetail!!.toList())
            showYourOrders()
        }
        else{

            showYourOrders()
            computeTotal(GlobalVariable.orderdetail!!.toList())
        }
    }

    val init={
        _binding.backArrowToMain.setOnClickListener {
            onBackPressed()
            overridePendingTransition(
                R.anim.screenslideleft, R.anim.screen_slide_out_right,
            );
            finish()
        }
        showYourOrders()
        computeTotal(GlobalVariable.orderdetail!!.toList())
    }


    val showYourOrders={
        checkoutadapter= CheckoutAdapter(this)
        _binding!!.rvItemCheckout.adapter=checkoutadapter
        _binding!!.rvItemCheckout.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        // _binding!!.rvSubitem.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        //_binding!!.rvRecommended.layoutManager = GridLayoutManager(requireActivity(), 2)
        if(GlobalVariable.orderdetail?.size!! >0)
            checkoutadapter.productItemsCheckout(GlobalVariable.orderdetail!!.toList())
//        else _binding!!.noItemTxt.isVisible=true
    }

    val computeTotal:(List<Orderdetail>)->Unit ={
        var total:Double=0.0
        it.forEach {
            total+= it.price
        }
        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(total)
        _binding!!.txtTotalAmount.text= formattedNumber
    }
}