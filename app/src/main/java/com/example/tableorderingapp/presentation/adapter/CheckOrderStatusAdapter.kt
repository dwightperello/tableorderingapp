package com.example.tableorderingapp.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tableorderingapp.databinding.AdapterAmountCheckBinding
import com.example.tableorderingapp.databinding.AdapterTotalorderBinding
import com.example.tableorderingapp.domain.model.request.Orderdetail
import com.example.tableorderingapp.presentation.activity.CheckoutActivity
import java.text.DecimalFormat

class CheckOrderStatusAdapter(private val activity: Activity): RecyclerView.Adapter<CheckOrderStatusAdapter.ViewHolder>() {
    private var allorders:List<Orderdetail> = listOf()

    class ViewHolder(view:AdapterAmountCheckBinding ) : RecyclerView.ViewHolder(view.root) {
        val quntity = view.tvQuantity
        val price = view.tvTotalprice
        val name= view.tvName
        val image=view.ivImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterAmountCheckBinding =
            AdapterAmountCheckBinding.inflate(LayoutInflater.from(activity),parent,false)
        return CheckOrderStatusAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allorders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orders= allorders[position]

        Glide.with(activity)
            .load(orders.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)

        val decimalFormatoriginal = DecimalFormat("#.00")
        val formattedNumberoriginal = decimalFormatoriginal.format(orders.originalPrice)

        holder.name.text = orders.orderTitle.plus(" - ${formattedNumberoriginal.toString()}")

        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(orders.price)
        holder.price.text = formattedNumber.toString()

        holder.quntity.text= orders.qty.toString().plus("x")
    }

    fun checkorders(list: List<Orderdetail>) {
        allorders = list
        notifyDataSetChanged()
    }
}