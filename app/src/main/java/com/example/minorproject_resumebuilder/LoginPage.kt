package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val login: Button =findViewById(R.id.buttonLogin);
        val regis: TextView =findViewById(R.id.textViewSignUp);
        val Username: EditText=findViewById(R.id.Username)
        val Password : EditText = findViewById(R.id.Password)
        val intent=intent
        val user = intent.getStringExtra("user")
        val pass = intent.getStringExtra("pass")
        val usernotexit :TextView = findViewById(R.id.usernotexit)



        login.setOnClickListener{
            val username=Username.text.toString()
            val password = Password.text.toString()
            if(username!=user || password!=pass ){
                usernotexit.setText("* Invalid username or password !!")
                Username.setText("")
                Password.setText("")
            }
            else{
                Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                val I= Intent(this,ViewPager::class.java)
                startActivity(I)
            }

        }
        regis.setOnClickListener{
            val I= Intent(this,RegistrationPage::class.java)
            startActivity(I)
        }
    }
}