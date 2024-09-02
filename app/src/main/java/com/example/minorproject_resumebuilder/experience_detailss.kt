package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper

class experience_detailss : AppCompatActivity() {

    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontain : LinearLayout
    private lateinit var db : SQLiteHelper
    var Resume_id : Long? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_experience_detailss)
        Resume_id = intent.getLongExtra("resume_id",1L)
        db = SQLiteHelper(this)
        addLayout= findViewById(R.id.addexperience)
        layoutcontain = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)

        addLayout.setOnClickListener{
            addExperience()
        }

        save.setOnClickListener{
            saveDetails()
        }

    }

    private fun saveDetails() {
        var value : Boolean = false
        for (i in 0 until layoutcontain.childCount){
            val educationView = layoutcontain.getChildAt(i)
            val jobTitle = educationView.findViewById<EditText>(R.id.jobTitle).text.toString()
            val companyName = educationView.findViewById<EditText>(R.id.companyName).text.toString()
            val companyLocation = educationView.findViewById<EditText>(R.id.comanyLocation).text.toString()
            val startDate = educationView.findViewById<EditText>(R.id.startDate).text.toString()
            value = db.insertExperience(Resume_id,companyName,companyLocation,startDate)
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
    fun addExperience(){
        val experienceDetailsView : View = LayoutInflater.from(this). inflate(R.layout.experience_details,layoutcontain,false)


        val delete : Button = experienceDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontain.removeView(experienceDetailsView)
                if(layoutcontain.childCount==0){
                    save.visibility= View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layoutcontain.addView(experienceDetailsView)
        save.visibility= View.VISIBLE
    }
}