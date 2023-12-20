package com.example.adminfoodyatra

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.adminfoodyatra.databinding.ActivityLoginBinding
import com.example.adminfoodyatra.model.userModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private var username:String ?=null
    private var nameOfRestaurant:String ?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient:GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        // Initialize GoogleSignInOptions
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth=Firebase.auth

        // Initialize GoogleSignInClient
        googleSignInClient=GoogleSignIn.getClient(this, googleSignInOptions)
        database=Firebase.database.reference
        binding.Loginbtn.setOnClickListener{
            email=binding.Email.text.toString().trim()
            password=binding.PasswordEditText.text.toString().trim()

            if (email.isBlank()||password.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)
            }
        }

        binding.googleButton.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.donthavebtn.setOnClickListener{
            val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            task->
            if(task.isSuccessful){
                val user=auth.currentUser
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task->
                    if (task.isSuccessful){
                        val user=auth.currentUser
                        Toast.makeText(this, "Create User & Login Successfully", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    }else{
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account","createUSerAccount: Authentication failed",task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email=binding.Email.text.toString().trim()
        password=binding.PasswordEditText.text.toString().trim()
        val user=userModel(username,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let{
            database.child("user").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    // Inside your Activity class
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    if (account != null) {
                        Toast.makeText(this, "Google Sign-In ", Toast.LENGTH_SHORT).show()
                        firebaseAuthWithGoogle(account)
                    } else {
                        Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Google SignIn is failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Successfully signed in with Google", Toast.LENGTH_SHORT).show()
                    updateUi(user)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    Log.d("GoogleSignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }
}