package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegistrationPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        val login: Button =findViewById(R.id.buttonRegister);
        val regis: TextView =findViewById(R.id.textViewLogin);
        val username : EditText = findViewById(R.id.editTextUsername)
        val emailId : EditText = findViewById(R.id.editTextEmail)
        val password : EditText = findViewById(R.id.editTextPassword)
        val phone : EditText = findViewById(R.id.ph)





        login.setOnClickListener{

            if(username.length()==0 || emailId.length()==0 || password.length()==0){
                username.setError("Username required")
                emailId.setError("EmailId required")
                password.setError("Password required")

            }

            else if(phone.length()!=10){
                phone.setError("10 digit phone number allowed")

            }
            else{
                val User= username.text.toString()
                val pass = password.text.toString()
                val intent= Intent(this,LoginPage::class.java).apply {
                    putExtra("user",User)
                    putExtra("pass",pass)
                }
                startActivity(intent)
            }
        }
        regis.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }
    }
}