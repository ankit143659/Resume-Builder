package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

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

        val resume_id = intent.getStringExtra("resume_id")?.toLong()

        btn_per.setOnClickListener{
            val intent = Intent(this,Basic_personal_details::class.java).apply{
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }

        btn_edu.setOnClickListener{
            val intent = Intent(this,Education_details::class.java).apply{
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }

        btn_skill.setOnClickListener{
            val intent = Intent(this,Skill_details::class.java).apply{
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }

        btn_exper.setOnClickListener{
            val intent = Intent(this,experience_detailss::class.java).apply{
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }

        btn_project.setOnClickListener{
            val intent = Intent(this,project_detailss::class.java).apply{
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }

        save.setOnClickListener{
            val intent = Intent(this,resume_template::class.java)
            startActivity(intent)
            finish()
        }
    }
}