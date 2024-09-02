package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper

class Skill_details : AppCompatActivity() {

    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var db : SQLiteHelper
    var Resume_id : Long? = null
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
        db = SQLiteHelper(this)
        Resume_id = intent.getLongExtra("resume_id",1L)

        addLayout.setOnClickListener{
            addskills();
        }

        save.setOnClickListener{
            savedata()
        }
    }

    private fun savedata() {
        var value : Boolean= false
        for (i in 0 until layoutcontainer.childCount){
            val educationView = layoutcontainer.getChildAt(i)
            val SkillName = educationView.findViewById<EditText>(R.id.skillName).text.toString()
            val a = educationView.findViewById<CheckBox>(R.id.begineer)
            val b = educationView.findViewById<CheckBox>(R.id.intermediate)
            val c = educationView.findViewById<CheckBox>(R.id.advance)

            var grade : String
            if (a.isChecked){
                grade = "Beginner"
            }else if (b.isChecked){
                grade = "Intermediate"
            }
            else{
                grade = "Advance"
            }
            value = db.insertSkill(Resume_id,SkillName,grade)
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun addskills(){
        val skillAddView : View = LayoutInflater.from(this). inflate(R.layout.skill_details,layoutcontainer,false)


        val delete : Button = skillAddView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontainer.removeView(skillAddView)
                if(layoutcontainer.childCount==0){
                    save.visibility=View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layoutcontainer.addView(skillAddView)
        save.visibility= View.VISIBLE
    }
}

