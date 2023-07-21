package com.example.tableorderingapp.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tableorderingapp.databinding.RecommendedLayoutBinding
import com.example.tableorderingapp.domain.model.response.Submenu
import com.example.tableorderingapp.presentation.activity.CheckoutActivity
import java.text.DecimalFormat

class RecomendedAdapter(private val activity: Activity): RecyclerView.Adapter<RecomendedAdapter.ViewHolder>()  {
    private var allSubmenuItemRecommended:List<Submenu> = listOf()

    class ViewHolder(view: RecommendedLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val ivAllImage = view.ivRecommendedImage
        val tvAllTitle = view.tvRecommendedName
        val tvAllPrice = view.txtitempriceRecommended
        val btnadd= view.btnAddcartRecommended
        val info= view.ivCartInfoRecommended



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecommendedLayoutBinding =
            RecommendedLayoutBinding.inflate(LayoutInflater.from(activity),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allSubmenuItemRecommended.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menus= allSubmenuItemRecommended[position]
        if(menus.isAvailable==false){
            holder.btnadd.setText("N/A")
            holder.btnadd.isEnabled=false
        }
        Glide.with(activity)
            .load(menus.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivAllImage)


        holder.tvAllTitle.text = menus.name
        val decimalFormat = DecimalFormat("#.00")
        val formattedNumber = decimalFormat.format(menus.price)
        holder.tvAllPrice.text = formattedNumber.toString()



        holder.btnadd.setOnClickListener {
            if(activity is CheckoutActivity){
                activity.AdditionalORder(menus)

            }
        }
    }
    fun productItemsRecommended(list: List<Submenu>) {
        allSubmenuItemRecommended = list
        notifyDataSetChanged()
    }
}