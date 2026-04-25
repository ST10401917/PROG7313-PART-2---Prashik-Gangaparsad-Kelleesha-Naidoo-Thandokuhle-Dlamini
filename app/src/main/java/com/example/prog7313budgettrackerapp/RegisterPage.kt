package com.example.prog7313budgettrackerapp

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

class RegisterPage : AppCompatActivity() {

    //global declaration of variables
    //This allows us to use them in multiple functions
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var toLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registerButton)
        toLoginButton = findViewById(R.id.toLoginButton)

        //Awaiting the database from my teammate


        registerButton.setOnClickListener{
            registerUser()
        }

        toLoginButton.setOnClickListener{
            toLoginPage()
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun registerUser() {
        val username = username.text.toString().trim()
        val password = password.text.toString().trim()
        val confirmPassword = confirmPassword.text.toString().trim()


    //Creating the validation for the password
    //checking if both fields are empty

    if(username.isEmpty() || password.isEmpty()){
        Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
        return
    }
        //Checking if password matches the confirmation password
        if (password != confirmPassword){
            Toast.makeText(this, "Please have the passwords match", Toast.LENGTH_SHORT).show()
            return
        }

        //Need the db to work


        }

    private fun toLoginPage(){
        toLogin()
    }

    private fun clearFields(){
        username.text.clear()
        password.text.clear()
        confirmPassword.text.clear()
    }

    private fun toLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // This closes the register page so the users cannot go back
    }
}
