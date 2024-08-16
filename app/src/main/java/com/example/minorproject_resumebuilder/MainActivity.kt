package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class MainActivity : AppCompatActivity() {

    private lateinit var prefrence: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val continuee :  Button=findViewById(R.id.continuee)
        prefrence = SharePrefrence(this)
        if(prefrence.isLoggedIn()){
            val I=Intent(this@MainActivity,HomePage::class.java)
            startActivity(I)
            finish()
        }
        continuee.setOnClickListener{
            val I=Intent(this@MainActivity,Continue_page::class.java)
            startActivity(I)
        }
        
    }

}