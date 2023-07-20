package com.example.tableorderingapp.presentation.adapter

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tableorderingapp.databinding.SubmenuitemAdapterBinding
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.presentation.activity.MainActivity
import com.example.tableorderingapp.util.GlobalVariable
import com.google.gson.Gson
import java.text.DecimalFormat

class SubMenuItemAdapter(private val activity: Activity): RecyclerView.Adapter<SubMenuItemAdapter.ViewHolder>() {
    private var allsubmenu:List<Submenu> = listOf()
    var originalAmountToInclude:Double=0.0
    private var newItemorder:Submenu?= null
    var amount:Double=0.0
    var sum:Double=0.0
    var intValue:Int=0
    var tag: Int=0
    var ordername:String?=null
    var gson= Gson()

    class ViewHolder(view: SubmenuitemAdapterBinding) : RecyclerView.ViewHolder(view.root) {
        val ivAllImage = view.ivSubMenuImage
        val tvAllTitle = view.txtName
        val price=view.txtPrice
        val description=view.txtDesciption
        val addtoorder=view.btnAddToOrder
        val btncheckout= view.btnCheckout
        val tag=view.txtTag
        val opacityframe=view.frameOpacity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:SubmenuitemAdapterBinding=
            SubmenuitemAdapterBinding.inflate(LayoutInflater.from(activity),parent,false)
        return SubMenuItemAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allsubmenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menus= allsubmenu[position]
        Glide.with(activity)
            .load(menus.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivAllImage)

        holder.tvAllTitle.text = menus.name
        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(menus.price)
        holder.price.text=formattedNumber.toString()
        holder.description.text=  truncateString(menus.description,550)


        if(menus.isRecommended==true){
            holder.tag.isVisible=true
        }
        if(menus.isAvailable) {
            holder.addtoorder.text="Add to order"
            holder.addtoorder.isEnabled=true
        }
        else {
            holder.addtoorder.text="N/A"
            holder.addtoorder.isEnabled=false
        }
        if(!GlobalVariable.orderdetail.isNullOrEmpty()){
            holder.btncheckout.isVisible=true
            holder.btncheckout.text= "Checkout ${GlobalVariable.orderdetail!!.size}"
        }

      //   holder.tvAllPrice.text = menus.price.toString()
        amount= menus.price
        originalAmountToInclude=menus.price

        holder.addtoorder.setOnClickListener {
            if(activity is MainActivity){
                activity.showbottomfragment(menus)
            }
        }

        holder.btncheckout.setOnClickListener {
            if(activity is MainActivity){
                activity.showCheckout()
            }
        }
    }

    fun subproductItems(list: List<Submenu>) {
        allsubmenu = list
        notifyDataSetChanged()
    }

    private fun truncateString(text: String, length: Int): String? {
        return if (text.length <= length) {
            text+"     "
        } else {
            text.substring(0, length)+"....."
        }
    }

  fun reloadItems(){
      notifyDataSetChanged()
  }

}