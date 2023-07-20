package com.example.tableorderingapp.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tableorderingapp.databinding.ActivityMainBinding
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.presentation.adapter.AllMenuAdapter
import com.example.tableorderingapp.presentation.adapter.SubMenuItemAdapter
import com.example.tableorderingapp.presentation.viewmodel.Service_viewmodel
import com.example.tableorderingapp.util.GlobalVariable
import com.example.tableorderingapp.util.ResultState
import com.example.tableorderingapp.util.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var _binding:ActivityMainBinding
    private val viewModel: Service_viewmodel by viewModels()
    private lateinit var allmenuitemadapater: AllMenuAdapter
    private lateinit var subMenuItemAdapter: SubMenuItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        viewModel.allmenu.observe(this,{
                state -> ProcessAllItemsResponse(state)
        })
        subMenuItemAdapter= SubMenuItemAdapter(this)
    }


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
               // if(TempMngr.recomendedItem.isNullOrEmpty()) PopulateRecommendedItems(state.data!!)
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

    fun showbottomfragment(menus: Submenu) {
        GlobalVariable.itemToaddToCart=menus
        val intent = Intent(this, AddToCartActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        subMenuItemAdapter.reloadItems()
    }
}