package com.example.adminfoodyatra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodyatra.Adapter.MenuItemAdapter
import com.example.adminfoodyatra.databinding.ActivityAllItemBinding
import com.example.adminfoodyatra.model.AllMenue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItem: ArrayList<AllMenue> = ArrayList()

    private val binding: ActivityAllItemBinding by lazy{
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.backButton.setOnClickListener{
            finish()
        }
        databaseReference=FirebaseDatabase.getInstance().reference
        retreveMenuItem()


    }

    private fun retreveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")

        //fetch data from Database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existng data before populating
                menuItem.clear()
                //loop for through each food
                for (foodSnapshot in snapshot.children){
                    val menuItems =foodSnapshot.getValue(AllMenue::class.java)
                    menuItems?.let{
                        menuItem.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
               Log.d("DatabaseError","Error:${error.message}")
            }

        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity,menuItem,databaseReference){
            position ->
            deleteMenuItem(position)
        }
        binding.MenuRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter=adapter
    }

    private fun deleteMenuItem(position: Int) {
val menuItemToDelete=menuItem[position]
        val menuItemKey=menuItemToDelete.key
        val foodMenuReference=database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener {task->
            if(task.isSuccessful){
                menuItem.removeAt(position)
                binding.MenuRecyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}