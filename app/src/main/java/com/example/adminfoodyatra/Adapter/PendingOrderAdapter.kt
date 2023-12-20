package com.example.adminfoodyatra.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodyatra.PendingOrdersActivity
import android.content.Context
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import com.example.adminfoodyatra.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val Image: MutableList<String>,
    private val itemClicked: OnItemClicked

)
    :RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
interface OnItemClicked{
    fun OnItemClickListener(position: Int)
    fun OnItemCAcceptlickListener(position: Int)
    fun OnItemDispatchClickListener(position: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int=customerNames.size

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
       holder.bind(position)
    }

    inner class PendingOrderViewHolder(private val binding:PendingOrdersItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccepted= false
        fun bind(position: Int) {
            binding.apply {
                CustomerName.text=customerNames[position]
                Log.d("Name", "List of Order Items: ${customerNames[position]}")
                pendingOrderQuantity.text=quantity[position]
                var uriString=Image[position]
                var uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(pendingImage)

                OrderedAcceptButton.apply {
                    if (!isAccepted){
                        text="Accept"
                    }else{
                        text="Dispatch"
                    }
                    setOnClickListener{
                        if(!isAccepted){
                            text="Dispatch"
                            isAccepted=true
                            showToast("Order is Accepted")
                            itemClicked.OnItemCAcceptlickListener(position)
                        }else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatched")
                            itemClicked.OnItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener{
                    itemClicked.OnItemClickListener(position)
                }
            }

        }
        private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }
}