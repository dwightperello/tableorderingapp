package com.example.tableorderingapp.presentation.activity

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityCheckoutBinding
import com.example.tableorderingapp.domain.converter.ConvertOrders
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.presentation.adapter.CheckoutAdapter
import com.example.tableorderingapp.presentation.adapter.RecomendedAdapter
import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.ResultState
import com.example.tableorderingapp.util.showCustomToast
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import java.text.DecimalFormat


@AndroidEntryPoint
class CheckoutActivity : BaseActivity() {
    private lateinit var _binding: ActivityCheckoutBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var checkoutadapter: CheckoutAdapter
    private lateinit var recommendedAdapter: RecomendedAdapter

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
        showRecommended()

        _binding.btnSubmitOrder.setOnClickListener {
            viewModel.postAllORder(ConvertOrders.createTableOrders(GlobalVariable.orderdetail!!.toList()))
            viewModel.postorder.observe(this,{
                    state -> ProcessCharResponse(state)
            })
        }
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

    fun AdditionalORder(submenu: Submenu){
        try {
            var orderdetail= Orderdetail(
                id = 0,
                orderTitle = submenu.name,
                orderTableTag = submenu.tag,
                price = submenu.price,
                qty = 1,
                tableOrderId = 0,
                originalPrice = submenu.price
            )
            GlobalVariable.orderdetail!!.add(orderdetail)


        }catch (e:Exception){
            Toast.makeText(this,"Error on ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        if(GlobalVariable.orderdetail!!.size>0){
            computeTotal(GlobalVariable.orderdetail!!.toList())

            showYourOrders()
        }

    }

    val showRecommended={
        recommendedAdapter= RecomendedAdapter(this)
        _binding!!.rvRecommended.adapter=recommendedAdapter
        // _binding!!.rvRecommended.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        // _binding!!.rvSubitem.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        _binding!!.rvRecommended.layoutManager = GridLayoutManager(this, 3)
        if(GlobalVariable.recomendedItem?.size!! >0){
            recommendedAdapter.productItemsRecommended(GlobalVariable.recomendedItem!!.toList())}

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

    private fun ProcessCharResponse(state: ResultState<ResponseBody>?){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{

                if(state.data!=null) {
                    hideProgressDialog()
                    GlobalVariable.orderdetail= arrayListOf()
                    GlobalVariable.recomendedItem = arrayListOf()
                    showdialog()
                }

            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)

            }
            else -> {}
        }

    }

    val showdialog={
        AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.SUCCESS)
            .setTitle("Success")
            .setMessage("Please wait for your order to complete. Thank you")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    onBackPressed()
                    overridePendingTransition(
                        R.anim.screenslideleft, R.anim.screen_slide_out_right,
                    );
                    finish()
                }
            })
            .show()
    }
}