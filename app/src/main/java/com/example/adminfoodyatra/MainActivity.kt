package com.example.adminfoodyatra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfoodyatra.databinding.ActivityAddItemBinding
import com.example.adminfoodyatra.databinding.ActivityMainBinding
import com.example.adminfoodyatra.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var completedOrderReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.addMenu.setOnClickListener{
            val intent=Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.AllItem.setOnClickListener{
            val intent=Intent(this,AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.OutForDeliveryButton.setOnClickListener{
            val intent=Intent(this,OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.Profile.setOnClickListener{
            val intent=Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.CreateNewUser.setOnClickListener{
            val intent=Intent(this,NewUserActivity::class.java)
            startActivity(intent)
        }
        binding.PendingOrder.setOnClickListener{
            val intent=Intent(this,PendingOrdersActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }

        pendingOrders()

        completedOrders()

        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        val listOfTotalPay= mutableListOf<Int>()
        completedOrderReference=FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    var completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()?.let{
                        i->
                        listOfTotalPay.add(i)
                    }

                }
                binding.whleTimeEarning.text=listOfTotalPay.sum().toString()+"$"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completedOrders() {
        val completeOrderReference=database.reference.child("CompletedOrder")
        var completeOrderItemCount=0
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount=snapshot.childrenCount.toInt()
                binding.completeOrder.text=completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
database=FirebaseDatabase.getInstance()
        val pendingOrderReference=database.reference.child("OrderDetails")
        var pendingOrderItemCount=0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.pendingOrders.text=pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}