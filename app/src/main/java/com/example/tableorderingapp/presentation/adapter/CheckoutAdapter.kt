package com.example.tableorderingapp.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tableorderingapp.databinding.AdapterTotalorderBinding
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.presentation.activity.CheckoutActivity
import java.text.DecimalFormat

class CheckoutAdapter(private val activity: Activity): RecyclerView.Adapter<CheckoutAdapter.ViewHolder>(){

    private var allorders:List<Orderdetail> = listOf()

    class ViewHolder(view: AdapterTotalorderBinding) : RecyclerView.ViewHolder(view.root) {
        val quntity = view.txtqtyCheckout
        val tvAllPrice = view.txtItemPriceCheckout
        val delete= view.cardViewTag
        val name= view.txtItemNameCheckout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterTotalorderBinding =
            AdapterTotalorderBinding.inflate(LayoutInflater.from(activity),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allorders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menus= allorders[position]
        holder.name.text = menus.orderTitle
        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(menus.originalPrice)
        holder.tvAllPrice.text = formattedNumber.toString()
        holder.quntity.text= menus.qty.toString().plus("x")
        holder.delete.setOnClickListener {
            if(activity is CheckoutActivity){
               activity.deleteItem(menus)

            }
        }
    }
    fun productItemsCheckout(list: List<Orderdetail>) {
        allorders = list
        notifyDataSetChanged()
    }
}