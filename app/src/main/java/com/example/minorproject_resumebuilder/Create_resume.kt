package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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
        val btn_skill = findViewById<LinearLayout>(R.id.skill)
        val btn_exper = findViewById<LinearLayout>(R.id.experience)
        val btn_project = findViewById<LinearLayout>(R.id.projects)
        val save  : Button = findViewById(R.id.save)

        btn_per.setOnClickListener{
            val intent = Intent(this,Basic_personal_details::class.java)
            startActivity(intent)
        }

        btn_edu.setOnClickListener{
            val intent = Intent(this,Education_details::class.java)
            startActivity(intent)
        }

        btn_skill.setOnClickListener{
            val intent = Intent(this,Skill_details::class.java)
            startActivity(intent)
        }

        btn_exper.setOnClickListener{
            val intent = Intent(this,Skill_details::class.java)
            startActivity(intent)
        }

        btn_project.setOnClickListener{
            val intent = Intent(this,Skill_details::class.java)
            startActivity(intent)
        }

        save.setOnClickListener{

        }
    }
}