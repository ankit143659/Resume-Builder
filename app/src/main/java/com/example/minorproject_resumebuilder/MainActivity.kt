package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val continuee :  Button=findViewById(R.id.continuee)
        continuee.setOnClickListener{
            val I=Intent(this@MainActivity,Continue_page::class.java)
            startActivity(I)
        }
        
    }

}