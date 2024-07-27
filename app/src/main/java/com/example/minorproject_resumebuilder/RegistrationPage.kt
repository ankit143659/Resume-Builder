package com.example.minorproject_resumebuilder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RegistrationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        val login: Button =findViewById(R.id.buttonRegister);
        val regis: TextView =findViewById(R.id.textViewLogin);

        login.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }
        regis.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }
    }
}