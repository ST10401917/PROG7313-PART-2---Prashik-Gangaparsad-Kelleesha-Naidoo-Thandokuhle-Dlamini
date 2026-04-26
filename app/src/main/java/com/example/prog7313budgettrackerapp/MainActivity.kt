package com.example.prog7313budgettrackerapp

import Data.database.AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    //global declarations
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginbutton: Button
    private lateinit var registerbutton: Button
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Room database
        db = AppDatabase.getDatabase(this)

        //Typecasting
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginbutton = findViewById(R.id.loginbutton)
        registerbutton = findViewById(R.id.registerbutton)

        //Set click listeners
        loginbutton.setOnClickListener {
           loginUser()
        }

        registerbutton.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private  fun loginUser(){
        val username = username.text.toString().trim()
        val password = password.text.toString().trim()

        //Validation
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            //check if user exists with the entered username and password
            val foundUser = db.userDao().loginUser(username, password)

            runOnUiThread {
                if (foundUser != null) {
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    //Go to the home screen once successful
                    openHomePage(foundUser.username)

                } else{
                    Toast.makeText(this@MainActivity, "Login Failed!", Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

    private fun openHomePage(username: String){
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        finish()
    }

}
