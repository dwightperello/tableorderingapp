package com.example.tableorderingapp.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tableorderingapp.databinding.MenuDishLayoutBinding
import com.example.tableorderingapp.domain.model.response.AllMenuModelItem
import com.example.tableorderingapp.presentation.activity.MainActivity

class AllMenuAdapter(private val activity: Activity): RecyclerView.Adapter<AllMenuAdapter.ViewHolder>() {
    private var allmenu= ArrayList<AllMenuModelItem>()

    class ViewHolder(view: MenuDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivAllImage = view.ivDishImage
        val tvAllTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:MenuDishLayoutBinding=
            MenuDishLayoutBinding.inflate(LayoutInflater.from(activity),parent,false)
        return AllMenuAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allmenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menus= allmenu[position]
        Glide.with(activity)
            .load(menus.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivAllImage)

        holder.tvAllTitle.text = menus.name
        // holder.tvAllPrice.text = menus.price.toString()
        holder.itemView.setOnClickListener {
            if(activity is MainActivity){
                activity.showsubmenu(menus)
            }
        }
    }

    fun productItems(list: ArrayList<AllMenuModelItem>) {
        allmenu = list
        notifyDataSetChanged()
    }
}