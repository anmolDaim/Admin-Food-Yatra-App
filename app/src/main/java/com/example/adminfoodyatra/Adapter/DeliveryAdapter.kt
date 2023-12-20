package com.example.adminfoodyatra.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodyatra.databinding.DeliveryItemBinding
import java.util.ArrayList

class DeliveryAdapter(private val customerNames: MutableList<String>, private val StatusMoney:MutableList<Boolean>): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int =customerNames.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class DeliveryViewHolder(private val  binding: DeliveryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.apply {
                customerName.text=customerNames[position]
                if(StatusMoney[position]==true){
                    MoneyStatus.text="Received"
                }else{
                    MoneyStatus.text="NotReceived"
                }
                val colorMap = mapOf(
                    true to Color.GREEN,false to Color.RED
                )
                MoneyStatus.setTextColor(colorMap[StatusMoney[position]]?:Color.BLACK)
                StatusColor.backgroundTintList= ColorStateList.valueOf(colorMap[StatusMoney[position]]?:Color.BLACK)
            }
        }
    }
}