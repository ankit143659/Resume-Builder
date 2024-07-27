package com.example.minorproject_resumebuilder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Continue_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_page)
        val login:Button=findViewById(R.id.buttonLogin);
        val regis:Button=findViewById(R.id.buttonSignUp);

        login.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }
        regis.setOnClickListener{
            val I= Intent(this,RegistrationPage::class.java)
            startActivity(I)
        }

    }
}