package com.example.adminfoodyatra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodyatra.Adapter.PendingOrderAdapter
import com.example.adminfoodyatra.databinding.ActivityPendingOrdersBinding
import com.example.adminfoodyatra.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrdersActivity : AppCompatActivity(),PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrdersBinding
    private var listOfNames: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //Initialization of database
        database = FirebaseDatabase.getInstance()

        databaseOrderDetails = database.reference.child("OrderDetails")

        databaseOrdersDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun databaseOrdersDetails() {
        //retrive order details from firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }

                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            // add data to respective list for populating the recyclerView
            orderItem.userName?.let { listOfNames.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {

                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.PendingOrderRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=PendingOrderAdapter(this,listOfNames,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.PendingOrderRecyclerView.adapter=adapter
    }

    override fun OnItemClickListener(position: Int) {
        val intent=Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails=listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun OnItemCAcceptlickListener(position: Int) {
        val childItemPushKey=listOfOrderItem[position].itemPushKey
        val clickItemOrdrRederence=childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrdrRederence?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickedItem=listOfOrderItem[position].userUid
        val pushKeyOfClickedItem=listOfOrderItem[position].itemPushKey
        val buyHistoryReference=database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }

    override fun OnItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey=listOfOrderItem[position].itemPushKey
        val dispatchItemorderReference=database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemorderReference.setValue(listOfOrderItem[position]).addOnSuccessListener {
            deleteThisItemFromOrderDetails(dispatchItemPushKey)
        }

    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemReference=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener(){
                Toast.makeText(this, "Order is not Dispatched", Toast.LENGTH_SHORT).show()
            }
    }
}