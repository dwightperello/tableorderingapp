package com.example.tableorderingapp.presentation.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.ActivityMainBinding
import com.example.tableorderingapp.domain.converter.ConvertNeedAssistance
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.AssistanceStatusResponse
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.presentation.adapter.AllMenuAdapter
import com.example.tableorderingapp.presentation.adapter.SubMenuItemAdapter
import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.ResultState
import com.example.tableorderingapp.util.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import java.time.temporal.Temporal

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var _binding:ActivityMainBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var allmenuitemadapater: AllMenuAdapter
    private lateinit var subMenuItemAdapter: SubMenuItemAdapter
    private lateinit var sharedPreferences: SharedPreferences

    var menuitemClicked:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        GlobalVariable.tablenumber= sharedPreferences.getInt("tablenumber",0)

        init()
    }

    //region FETCH DATA
    val init={
        viewModel.allmenu.observe(this,{
                state -> ProcessAllItemsResponse(state)
        })

        subMenuItemAdapter= SubMenuItemAdapter(this)

        _binding.ivSettings.setOnClickListener {
            showSettingsPin()
        }

        _binding.txtCallassistance.setOnClickListener {
            if(!menuitemClicked) showDialogbox()
            else showDialogboxCancelassistance()

        }
    }

    //endregion

    //region ALL VIEW
    private fun ProcessAllItemsResponse(state: ResultState<ArrayList<AllMenuModelItem>>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                val black = "#000000"
                val blackColorInt = Color.parseColor(black)
                val superLightBlack = Color.argb(50, Color.red(blackColorInt), Color.green(blackColorInt), Color.blue(blackColorInt))
                _binding.bottomBar.setBackgroundColor(superLightBlack)

                allmenuitemadapater= AllMenuAdapter(this)
                _binding!!.rvAllmenu.adapter = allmenuitemadapater
                // _binding!!.rvRecomendeddishesList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
                _binding!!.rvAllmenu.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
                state.data?.let { allmenuitemadapater.productItems(it) }
                if(GlobalVariable.recomendedItem.isNullOrEmpty()) PopulateRecommendedItems(state.data!!)
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    fun showsubmenu(menus: AllMenuModelItem) {
        var submenu= menus.submenu
        _binding!!.rvSubmenu.adapter = subMenuItemAdapter
        // _binding!!.rvRecomendeddishesList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
        _binding!!.rvSubmenu.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        submenu.let { subMenuItemAdapter.subproductItems(submenu!!) }
    }

    //endregion

    //region NAVIGATION
    fun showbottomfragment(menus: Submenu) {
        GlobalVariable.itemToaddToCart=menus
        val intent = Intent(this, AddToCartActivity::class.java)
        startActivity(intent)
    }
    fun showCheckout() {
        val intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screenslideright,
        R.anim.screen_slide_out_left);
    }

    //endregion

    //region SETTING TABLE NUMBER
    var tablenumber:String?= null

    val showSettingsPin:()->Unit={

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogbox, null)
        val pintext= dialogView.findViewById<EditText>(R.id.pineditText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setView(dialogView)
        builder.setTitle("Please enter pin")
        builder.setCancelable(false)
        builder.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                val userpin= pintext.text.toString()
                if(userpin.equals("2910")) {
                   enterTableNumberDialog()
                }
                else Toast.makeText(this,"Wrong pin",Toast.LENGTH_LONG).show()

            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                // If user click no then dialog box is canceled.
                dialog.cancel()
            })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    val enterTableNumberDialog:()->Unit={

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogbox, null)
        val pintext= dialogView.findViewById<EditText>(R.id.pineditText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setView(dialogView)
        builder.setTitle("Enter new table number")
        builder.setCancelable(false)
        builder.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                tablenumber= pintext.text.toString()
                val editor=sharedPreferences.edit()
                editor.putInt("tablenumber", tablenumber!!.toInt())
                editor.apply()
                GlobalVariable.tablenumber=tablenumber!!.toInt()


            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                // If user click no then dialog box is canceled.
                dialog.cancel()
            })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
    //endregion

    //region ASSISTANCE CALL
    private fun ProcessAssistanceResponse(state: ResultState<ResponseBody>){
        when(state){
            is ResultState.Loading ->{}


            is ResultState.Success->{
                if(menuitemClicked){
                    _binding.txtCallassistance.setTextColor ( Color.parseColor("#EB1F5D"))
                    watchAssistanceStatus()
                }
                else  _binding.txtCallassistance.setTextColor ( Color.parseColor("#FFFFFFFF"))

                // watchAssistanceStatus(menuitem)
            }
            is ResultState.Error->{

                Toast(this).showCustomToast(state.exception.toString(), this)
            }
            else -> {}
        }
    }

    val showDialogbox:()->Unit={
        val dialogView = LayoutInflater.from(this).inflate(androidx.customview.R.layout.custom_dialog, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Do you need assistance?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                menuitemClicked=true
                viewModel.callassistance.observe(this,{
                        state -> ProcessAssistanceResponse(state)
                })
                viewModel.callAssistance(GlobalVariable.tablenumber!!,ConvertNeedAssistance.updateAssistance(true))

            })
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                // If user click no then dialog box is canceled.
                dialog.cancel()
            })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    val showDialogboxCancelassistance:()->Unit={
        val dialogView = LayoutInflater.from(this).inflate(androidx.customview.R.layout.custom_dialog, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel assistance?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                menuitemClicked=false
                viewModel.callAssistance(GlobalVariable.tablenumber!!,ConvertNeedAssistance.updateAssistance(false))
            })
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                dialog.cancel()
            })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    val handler: Handler = Handler()
    val delay = 10000 // 1000 milliseconds == 1 second

    val watchAssistanceStatus:()-> Unit={
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.assistancestatus.observe(this@MainActivity,{
                        state->ProcessWatchAssistance(state)
                })

                viewModel.getAssistanceStatus(GlobalVariable.tablenumber!!.toInt())
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    private fun ProcessWatchAssistance(state: ResultState<AssistanceStatusResponse>){
        when(state){
            is ResultState.Loading ->{}
            is ResultState.Success->{
                state.data?.let {
                    if(it.needAssistance==false) {
                        menuitemClicked=false
                        _binding.txtCallassistance.setTextColor(Color.parseColor("#FFFFFFFF"))
                        handler.removeCallbacksAndMessages(null);
                    }
                    else {
                        _binding.txtCallassistance.setTextColor(Color.parseColor("#EB1F5D"))
                        menuitemClicked=true
                    }
                }
                Log.d("assistancestatur",state.data.needAssistance.toString())
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }


    //endregion


    val PopulateRecommendedItems:(List<AllMenuModelItem>)->Unit={
        it.forEach {
            it.submenu.forEach {
                if (it.isRecommended == true) {
                    var items = Submenu(
                        id = it.id,
                        description = it.description,
                        imageURL = it.imageURL,
                        isRecommended = it.isRecommended,
                        price = it.price,
                        menuId = it.menuId,
                        name = it.name,
                        tag = it.tag,
                        isAvailable = it.isAvailable
                    )
                    GlobalVariable.recomendedItem?.add(items)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subMenuItemAdapter.reloadItems()
    }
}