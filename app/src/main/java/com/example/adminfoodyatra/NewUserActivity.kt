package com.example.adminfoodyatra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfoodyatra.databinding.ActivityAdminProfileBinding
import com.example.adminfoodyatra.databinding.ActivityAllItemBinding
import com.example.adminfoodyatra.databinding.ActivityNewUserBinding

class NewUserActivity : AppCompatActivity() {
    private val binding: ActivityNewUserBinding by lazy{
        ActivityNewUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.backButton.setOnClickListener{
            finish()
        }
    }
}