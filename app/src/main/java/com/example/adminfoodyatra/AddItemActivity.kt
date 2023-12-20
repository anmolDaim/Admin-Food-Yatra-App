package com.example.adminfoodyatra

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfoodyatra.databinding.ActivityAddItemBinding
import com.example.adminfoodyatra.databinding.ActivityLoginBinding
import com.example.adminfoodyatra.model.AllMenue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy{
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()

        binding.AddItembutton.setOnClickListener {
            // Get Data from Filed
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.EnterFoodPrice.text.toString().trim()
            foodDescription = binding.foodDescription.text.toString().trim()
            foodIngredient= binding.foodingrediant.text.toString().trim()
            if(!(foodName.isBlank()||foodPrice.isBlank()||foodDescription.isBlank()||foodIngredient.isBlank())){
                uploadData()
                Toast.makeText(this, "Items Added Syccessfuly", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Fill All the details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.foodImage.setOnClickListener{
            pickImage.launch("image/*")
        }
        binding.backButton.setOnClickListener{
            finish()
        }
    }

    private fun uploadData() {
        //get reference to the menu node in the firebase
        val menuRef=database.getReference("menu")
        //Generate a unique key for the new menu item
        val newItemKey=menuRef.push().key

        if(foodImageUri!=null){
            val storageRef=FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("menu_image${newItemKey}.jpg")
            val uploadTask=imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    val newItem=AllMenue(
                        newItemKey,
                        foodName=foodName,
                        foodPrice=foodPrice,
                        foodDescription=foodDescription,
                        foodIngrediant=foodIngredient,
                        foodImage=downloadUrl.toString()
                    )
                    newItemKey?.let{
                        key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Datat uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "data uploaded failde", Toast.LENGTH_SHORT).show()
                            }

                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this, "image uploaded failde", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage= registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        if(uri!= null){
            binding.SelectedImage.setImageURI(uri)
            foodImageUri=uri
        }
    }
}