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

class project_detailss : AppCompatActivity() {
    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layout : LinearLayout
    private lateinit var db : SQLiteHelper
    var resume_id : Int? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_project_detailss)
        val Resume_id = intent.getStringExtra("resume_id")
        resume_id = Resume_id?.toInt()
        db = SQLiteHelper(this)

        addLayout= findViewById(R.id.addProjects)
        layout = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)

        addLayout.setOnClickListener{
            addEducation()
        }

        save.setOnClickListener{
           saveData()
        }

    }

    private fun saveData() {
        var value : Boolean = false
        for (i in 0 until layout.childCount){
            val educationView = layout.getChildAt(i)
            val projectName = educationView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = educationView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = educationView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = educationView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = educationView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = educationView.findViewById<EditText>(R.id.endDate).text.toString()
            value = db.insertProject(resume_id,projectName,description,projectUrl,startDate,endDate,role)
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addEducation(){
        val educationDetailsView : View = LayoutInflater.from(this). inflate(R.layout.project,layout,false)


        val delete : Button = educationDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layout.removeView(educationDetailsView)
                if(layout.childCount==0){
                    save.visibility= View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layout.addView(educationDetailsView)
        save.visibility= View.VISIBLE
    }
}