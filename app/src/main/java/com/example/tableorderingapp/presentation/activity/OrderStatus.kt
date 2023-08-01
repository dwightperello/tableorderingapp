package com.example.tableorderingapp.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityOrderStatusBinding
import com.example.tableorderingapp.domain.converter.convertPaymaya
import com.example.tableorderingapp.domain.converter.convertUpdateccpayment
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.response.CCpaymentOrderDetails
import com.example.tableorderingapp.presentation.adapter.CheckOrderStatusAdapter
import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.ResultState
import com.paymaya.sdk.android.checkout.PayMayaCheckout
import com.paymaya.sdk.android.common.LogLevel
import com.paymaya.sdk.android.common.PayMayaEnvironment
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody


@AndroidEntryPoint
class OrderStatus : BaseActivity() {

    private lateinit var _binding: ActivityOrderStatusBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var checkOrderStatusAdapter: CheckOrderStatusAdapter
    var orders:List<Orderdetail>?= null

    val payMayaCheckoutClient = PayMayaCheckout.newBuilder()
        .clientPublicKey("pk-Z0OSzLvIcOI2UIvDhdTGVVfRSSeiGStnceqwUE7n0Ah")
        .environment(PayMayaEnvironment.SANDBOX)
        .logLevel(LogLevel.ERROR)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding= ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        payMayaCheckoutClient
        init()
        _binding.btnPaycc.setOnClickListener {
            var ch= convertPaymaya.BuildPaymayaCheckoutRequest()
            payMayaCheckoutClient.startCheckoutActivityForResult(this,ch)
        }
    }

    val init={
        viewModel.PaybyCard(GlobalVariable.tablenumber!!)

        _binding.backArrowToMain.setOnClickListener {
            backpress()
        }


    }

    override fun onStart() {
        super.onStart()

        viewModel.paybycard.observe(this, Observer {
            state-> ProcessPaybCard(state)
        })

        viewModel.updateccpayment.observe(this, Observer {
            state -> Processupdatestatus(state)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        payMayaCheckoutClient.onActivityResult(requestCode, resultCode, data)?.let {
            when(resultCode){
                -1->{

                    viewModel.updateCCpayment(GlobalVariable.cCpaymentOrderDetails!!.id,convertUpdateccpayment.updateccpayment(GlobalVariable.cCpaymentOrderDetails!!.id,true))

                }
                else ->{
                    Toast.makeText(this,"GAGO∂",Toast.LENGTH_LONG).show()
                }
            }

            return
        }
    }

    private fun Processupdatestatus(state: ResultState<ResponseBody>){
        when(state){
            is ResultState.Loading ->{

            }
            is ResultState.Success->{


                showdialog()

            }
            is ResultState.Error->{
                hideProgressDialog()
                if(!isWatchStarted)
                    watchPaybycardDetails()


            }
            else -> {}
        }
    }
    private fun ProcessPaybCard(state: ResultState<CCpaymentOrderDetails>){
        when(state){
            is ResultState.Loading ->{

            }
            is ResultState.Success->{

                if(state.data!=null){
                    GlobalVariable.cCpaymentOrderDetails= state.data
                    handler.removeCallbacksAndMessages(null);
                    isWatchStarted=false
                    checkOrderStatusAdapter= CheckOrderStatusAdapter(this)
                _binding!!.rvCcDetails.adapter = checkOrderStatusAdapter
                _binding!!.rvCcDetails.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                state.data?.let {
                    checkOrderStatusAdapter.checkorders(state.data.creditcardorderdetails!!)
                }
                    _binding.framLeft.isVisible=true
                    _binding.txtWarning.isVisible=false
                    _binding.rvCcDetails.isVisible=true
                    _binding.txtTableNum.text="Table #: ${state.data.tableNumber}"
                    _binding.txtDate.text=state.data.dateTimeStamp
                    _binding.txtVat.text= "Vat: ${state.data.vat}"
                    _binding.txtVatAmount.text="Vat amount: ${state.data.vatAmount}"
                    _binding.txtDiscount.text="Vat exempt: ${state.data.discountAmount}"
                    _binding.txtServiceCharge.text= "Service Fee: ${state.data.serviceCharge}"
                    _binding.txtTotal.text="TOTAL: ${state.data.total}"

                }


            }
            is ResultState.Error->{
                hideProgressDialog()
                _binding.framLeft.isVisible=false
                _binding.txtWarning.isVisible=true
                _binding.rvCcDetails.isVisible=false
                if(!isWatchStarted)
                watchPaybycardDetails()


            }
            else -> {}
        }
    }

    val handler: Handler = Handler()
    val delay = 10000
    var isWatchStarted:Boolean=false

    val watchPaybycardDetails:()-> Unit={
        isWatchStarted=true
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.PaybyCard(GlobalVariable.tablenumber!!)
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }


    val backpress={
        onBackPressed()
        overridePendingTransition(
            R.anim.screenslideleft, R.anim.screen_slide_out_right,
        );
        finish()
    }



    val showdialog={
        AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.SUCCESS)
            .setTitle("Payment Success")
            .setMessage("Please wait for receipt. Thank you")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    backpress()
                }
            })
            .show()
    }


}