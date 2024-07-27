package com.example.minorproject_resumebuilder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val login: Button =findViewById(R.id.buttonLogin);
        val regis: TextView =findViewById(R.id.textViewSignUp);

        login.setOnClickListener{
            val I= Intent(this,ViewPager::class.java)
            startActivity(I)
        }
        regis.setOnClickListener{
            val I= Intent(this,RegistrationPage::class.java)
            startActivity(I)
        }
    }
}