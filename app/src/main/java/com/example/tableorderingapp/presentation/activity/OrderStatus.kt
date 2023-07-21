package com.example.tableorderingapp.presentation.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityOrderStatusBinding
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.request.PostTableOrder
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.presentation.adapter.AllMenuAdapter
import com.example.tableorderingapp.presentation.adapter.CheckOrderStatusAdapter
import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.ResultState
import com.example.tableorderingapp.util.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.time.temporal.Temporal

@AndroidEntryPoint
class OrderStatus : BaseActivity() {

    private lateinit var _binding: ActivityOrderStatusBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var checkOrderStatusAdapter: CheckOrderStatusAdapter
    var orders:List<Orderdetail>?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding= ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        init()
    }

    val init={
        viewModel.totalamount.observe(this,{
                state -> ProcessAllItemsResponse(state)
        })

        viewModel.getTotalAmount(GlobalVariable.tablenumber!!)

        _binding.backArrowToMain.setOnClickListener {
            backpress()
        }
    }

    private fun ProcessAllItemsResponse(state: ResultState<PostTableOrder>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                orders= state.data!!.orderdetails
                if(orders.isNullOrEmpty())_binding.txtYourOrders.text="Come back when you have ordered"
                val black = "#000000"
                val blackColorInt = Color.parseColor(black)
                val superLightBlack = Color.argb(100, Color.red(blackColorInt), Color.green(blackColorInt), Color.blue(blackColorInt))
                _binding.linearLeft.setBackgroundColor(superLightBlack)

                checkOrderStatusAdapter= CheckOrderStatusAdapter(this)
                _binding!!.rvCheckOrderStatus.adapter = checkOrderStatusAdapter
                _binding!!.rvCheckOrderStatus.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                state.data?.let {
                    checkOrderStatusAdapter.checkorders(orders!!)

                }
                val decimalFormat = DecimalFormat("#.00")
                val formattedNumber = decimalFormat.format(state.data.orderPrice)
                _binding.totalLinear.isVisible=true
                _binding.txtTotalAmount.text="Total: ${formattedNumber.toString()}"

            }
            is ResultState.Error->{
                hideProgressDialog()
                if(orders.isNullOrEmpty())_binding.txtYourOrders.text="Come back when you have ordered"
                _binding.totalLinear.isVisible=false
            }
            else -> {}
        }
    }

    val backpress={
        onBackPressed()
        overridePendingTransition(
            R.anim.screenslideleft, R.anim.screen_slide_out_right,
        );
        finish()
    }
}