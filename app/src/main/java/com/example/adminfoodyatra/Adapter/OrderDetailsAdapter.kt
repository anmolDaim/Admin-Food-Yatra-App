package com.example.adminfoodyatra.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.content.Context

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.adminfoodyatra.databinding.OrderDetailItemsBinding
import java.util.ArrayList

class OrderDetailsAdapter
    (private var context:Context,
     private var foodNames: ArrayList<String>,
     private var foodImage:ArrayList<String>,
     private var foodQuantity:ArrayList<Int>,
     private var foodPrice:ArrayList<String>):
    RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = OrderDetailItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailViewHolder(binding)
    }

    override fun getItemCount(): Int =foodNames.size

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(position)    }

    inner class OrderDetailViewHolder(private val binding:OrderDetailItemsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                FoodName.text=foodNames[position]
                FoodQuantity.text=foodQuantity[position].toString()
                val uriString=foodImage[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(FoodImage)
                FoodPrice.text=foodPrice[position]

            }
        }

    }
}
