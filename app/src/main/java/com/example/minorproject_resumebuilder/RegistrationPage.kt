package com.example.minorproject_resumebuilder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RegistrationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        val login: Button =findViewById(R.id.buttonRegister);
        val regis: TextView =findViewById(R.id.textViewLogin);
        val username : EditText = findViewById(R.id.editTextUsername)
        val emailId : EditText = findViewById(R.id.editTextEmail)
        val password : EditText = findViewById(R.id.editTextPassword)
        val cpassword : EditText = findViewById(R.id.editTextConfirmPassword)

        login.setOnClickListener{
            if(username.length()==0 || emailId.length()==0 || password.length()==0){
                username.setError("Username required")
                emailId.setError("EmailId required")
                password.setError("Password required")

            }
            else if(cpassword!==password){
                cpassword.setError("Password does not match")
            }
            else{
                val I= Intent(this,LoginPage::class.java)
                startActivity(I)
            }
        }
        regis.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }
    }
}