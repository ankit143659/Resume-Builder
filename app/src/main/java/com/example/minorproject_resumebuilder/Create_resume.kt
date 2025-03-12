package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Create_resume : AppCompatActivity() {
    private lateinit var share : SharePrefrence
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
        share = SharePrefrence(this)
        btn_per.setOnClickListener{
            val intent = Intent(this,Basic_personal_details::class.java)
            startActivity(intent)
        }

        btn_edu.setOnClickListener{
            val intent = Intent(this,Education_details::class.java)
            startActivity(intent)
        }

        btn_skill.setOnClickListener{
            val intent = Intent(this,SkillDetail::class.java)
            startActivity(intent)
        }

        btn_exper.setOnClickListener{
            val intent = Intent(this,experience_detailss::class.java)
            startActivity(intent)
        }

        btn_project.setOnClickListener{
            val intent = Intent(this,ProjectDetail::class.java)
            startActivity(intent)
        }

        save.setOnClickListener{
            val value : Boolean  = share.checkEdit()

            if (value){
                val intent = Intent(this,resume_template::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Plaese fill Personal Details atleast",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this,HomePage::class.java)
        startActivity(i)
        finish()
    }

}