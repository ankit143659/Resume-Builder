package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Preview_template : AppCompatActivity() {

    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var buttonContainer : LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview_template)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent(getIntent())
        val resumeName :String = intent.getStringExtra("value").toString()

        layoutcontainer = findViewById(R.id.layoutcontainer)
        buttonContainer = findViewById(R.id.buttonContainer)


        if (resumeName =="medical_1"){
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.medical_1,layoutcontainer,false)
            layoutcontainer.addView(resume_preview)
        }
        else{
            layoutcontainer.visibility=View.GONE
            buttonContainer.visibility=View.GONE
        }

        if (layoutcontainer.childCount!=0){
            buttonContainer.visibility=View.VISIBLE
        }


    }
}