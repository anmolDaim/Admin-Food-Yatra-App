package com.example.adminfoodyatra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminfoodyatra.databinding.ActivitySignupBinding
import com.example.adminfoodyatra.model.userModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var nameOfRestaurant:String
    private lateinit var database:DatabaseReference

    private val binding: ActivitySignupBinding by lazy{
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account","create Account :Failure",task.exception)
            }
        }
    }
//save data into database
    private fun saveUserData() {
    username = binding.newUsername.text.toString().trim()
    nameOfRestaurant = binding.restaurantName.text.toString().trim()
    email = binding.emailOrPhone.text.toString().trim()
    password = binding.password.text.toString().trim()
    val user = userModel(username, nameOfRestaurant, email, password)
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    //save user data firebase database
    if (userId != null) {
        database.child("user").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("SignupActivity", "User data saved successfully")
            }
            .addOnFailureListener {
                Log.e("SignupActivity", "Error saving user data: ${it.message}")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        //init firebase auth
        auth=Firebase.auth
        //init firebase database
        database=Firebase.database.reference

        binding.createUserButton.setOnClickListener{
            username=binding.newUsername.text.toString().trim()
            nameOfRestaurant=binding.restaurantName.text.toString().trim()
            email=binding.emailOrPhone.text.toString().trim()
            password=binding.password.text.toString().trim()

            if (username.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText( this,"Please fill All Details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }

        val locationList= arrayOf("jaipur","Panipat","Ambala","Indore","Mumbai")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
        binding.AlreadyHaveAnAcc.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}