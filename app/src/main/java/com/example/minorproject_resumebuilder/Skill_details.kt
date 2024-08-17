package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Skill_details : AppCompatActivity() {

    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skill_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addLayout=findViewById(R.id.addSkill)
        layoutcontainer=findViewById(R.id.layoutContainer)
        save=findViewById(R.id.savebtn)

        addLayout.setOnClickListener{
            addskills();
        }
    }

    fun addskills(){
        val skillAddView : View = LayoutInflater.from(this). inflate(R.layout.education_details,layoutcontainer,false)


        val delete : Button = skillAddView.findViewById(R.id.delete)
        delete.setOnClickListener{
            layoutcontainer.removeView(skillAddView)
            if(layoutcontainer.childCount==0){
                save.visibility= View.GONE
            }
        }

        layoutcontainer.addView(skillAddView)
        save.visibility= View.VISIBLE
    }
}

