package com.example.adminfoodyatra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminfoodyatra.databinding.ActivityAdminProfileBinding
import com.example.adminfoodyatra.model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
//    private lateinit var auth:FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var adminReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        auth=FirebaseAuth.getInstance()
//        database=FirebaseDatabase.getInstance()
//        adminReference=database.reference.child("user")

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.adminName.isEnabled=false
        binding.address.isEnabled=false
        binding.email.isEnabled=false
        binding.phoneNo.isEnabled=false
        binding.edPassword.isEnabled=false
        binding.saveInfoButton.isEnabled=false

        var isEnable=false
        binding.editButton.setOnClickListener{
            isEnable=!isEnable
            binding.adminName.isEnabled=isEnable
            binding.address.isEnabled=isEnable
            binding.email.isEnabled=isEnable
            binding.phoneNo.isEnabled=isEnable
            binding.edPassword.isEnabled=isEnable
            if (isEnable){
                binding.adminName.requestFocus()
            }
//            binding.saveInfoButton.isEnabled=isEnable
        }
        binding.backButton.setOnClickListener{
            finish()
        }
//        retriveUserAction()
    }


//    private fun retriveUserAction() {
//        val currentUserUid=auth.currentUser?.uid
//        if(currentUserUid!=null){
//            val userReference=adminReference.child(currentUserUid)
//            userReference.addListenerForSingleValueEvent(object:ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.exists()){
//                        var ownerName=snapshot.child("name").getValue()
//                        var email=snapshot.child("email").getValue()
//                        var password=snapshot.child("password").getValue()
//                        var address=snapshot.child("address").getValue()
//                        var phone=snapshot.child("phone").getValue()
//
//                        setDataToTextView(ownerName,email,password,address,phone)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }
//
//    }
//
//    private fun setDataToTextView(
//        ownerName: Any?,
//        email: Any?,
//        password: Any?,
//        address: Any?,
//        phone: Any?
//    ) {
//        binding.adminName.setText(ownerName.toString())
//        binding.email.setText(email.toString())
//        binding.edPassword.setText(password.toString())
//        binding.address.setText(address.toString())
//        binding.phoneNo.setText(phone.toString())
//    }
//
//    private fun updateUserData() {
//       var updatName= binding.adminName.text.toString()
//        var updateEmail= binding.email.text.toString()
//        var updatePassword= binding.edPassword.text.toString()
//        var updatePhone= binding.address.text.toString()
//        var updateAddress= binding.phoneNo.text.toString()
//        var userData= userModel(updatName,updateEmail,updatePassword,updatePhone,updateAddress)
//
//        val currentUserUid=auth.currentUser?.uid
//        if (currentUserUid!=null){
//            val userReference=adminReference.child(currentUserUid)
//            userReference.child("name").setValue(updatName)
//            userReference.child("email").setValue(updateEmail)
//            userReference.child("password").setValue(updatePassword)
//            userReference.child("phone").setValue(updatePhone)
//            userReference.child("address").setValue(updateAddress)
//            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
//            auth.currentUser?.updateEmail(updateEmail)
//            auth.currentUser?.updatePassword(updatePassword)
//        }
//       else {
//            Toast.makeText(this, "Profile not updated ", Toast.LENGTH_SHORT).show()
//        }
//    }
}