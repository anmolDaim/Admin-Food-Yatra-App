package com.example.adminfoodyatra.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodyatra.databinding.ItemItemBinding
import com.example.adminfoodyatra.model.AllMenue
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val contaxt: Context,
    private val menuList: ArrayList<AllMenue>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position:Int) ->Unit
): RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantities= IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding =ItemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddItemViewHolder(binding)
    }

    override fun getItemCount(): Int =menuList.size

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddItemViewHolder (private val binding:ItemItemBinding):RecyclerView.ViewHolder(binding.root)  {
        fun bind(position: Int) {
            binding.apply {
                val quantity=itemQuantities[position]
                val menuItem=menuList[position]
                val uriString=menuItem.foodImage
                val uri= Uri.parse(uriString)
                cartFoodName.text=menuItem.foodName
                cartItemPrice.text=menuItem.foodPrice
                Glide.with(contaxt).load(uri).into(foodImageView)
                cartItemQuantity.text=quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }
        private fun increaseQuantity(position:Int){
            if(itemQuantities[position]<10){
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }
        private fun deleteItem(position:Int){
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemChanged(position,menuList.size)
        }
        private fun decreaseQuantity(position:Int){
            if(itemQuantities[position]>1){
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

    }
}