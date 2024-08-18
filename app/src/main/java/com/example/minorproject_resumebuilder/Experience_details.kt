package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.Create_resume
import com.example.minorproject_resumebuilder.R

class Experience_details : AppCompatActivity() {
    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_experience_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addLayout= findViewById(R.id.addEducation)
        layoutcontainer = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)

        addLayout.setOnClickListener{
            addEducation()
        }

        save.setOnClickListener{
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("MissingInflatedId")
    fun addEducation(){
        val experienceDetailsView : View = LayoutInflater.from(this). inflate(R.layout.experience_details,layoutcontainer,false)


        val delete : Button = experienceDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.log_out,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontainer.removeView(experienceDetailsView)
                if(layoutcontainer.childCount==0){
                    save.visibility=View.GONE
                }
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layoutcontainer.addView(experienceDetailsView)
        save.visibility=View.VISIBLE
    }
}