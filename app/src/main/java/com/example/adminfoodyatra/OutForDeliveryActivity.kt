package com.example.adminfoodyatra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodyatra.Adapter.DeliveryAdapter
import com.example.adminfoodyatra.databinding.ActivityMainBinding
import com.example.adminfoodyatra.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodyatra.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy{
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteorderList:ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        binding.backButton.setOnClickListener{
            finish()
        }
        retriveCompleteOrderDetails()
    }

    private fun retriveCompleteOrderDetails() {
        database=FirebaseDatabase.getInstance()
        val CompleOrderReference=database.reference.child("CompletedOrder").orderByChild("currentItem")
        CompleOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteorderList.clear()

                for(orderSnapshot in snapshot.children){
                    val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let{
                        listOfCompleteorderList.add(it)
                    }
                }
                listOfCompleteorderList.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setDataIntoRecyclerView() {
        val customerName= mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()
        for(order in listOfCompleteorderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter= DeliveryAdapter(customerName,moneyStatus)
        binding.OutForDeliveryRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.OutForDeliveryRecyclerView.adapter=adapter

    }
}