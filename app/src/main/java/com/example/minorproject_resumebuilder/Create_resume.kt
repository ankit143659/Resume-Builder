package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Create_resume : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_resume)
        val btn_per = findViewById<LinearLayout>(R.id.personal)
        val btn_edu = findViewById<LinearLayout>(R.id.education)

        btn_per.setOnClickListener{
            val intent = Intent(this,Basic_personal_details::class.java)
            startActivity(intent)
        }

        btn_edu.setOnClickListener{
            val intent = Intent(this,Education_details::class.java)
            startActivity(intent)
        }
    }
}