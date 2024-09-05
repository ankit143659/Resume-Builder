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
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Education_details : AppCompatActivity() {
    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var db : SQLiteHelper
    var Resume_id : Long? = null
    private lateinit var share: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_education_details)
        share = SharePrefrence(this)
         Resume_id = share.getResumeId()
        addLayout= findViewById(R.id.addEducation)
        layoutcontainer = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)
        db = SQLiteHelper(this)

        addLayout.setOnClickListener{
            addEducation()
        }

        save.setOnClickListener{
            saveDetails()
        }

    }

    private fun saveDetails() {
        var value : Boolean = false
        for (i in 0 until layoutcontainer.childCount){
            val educationView = layoutcontainer.getChildAt(i)
            val degreeName = educationView.findViewById<EditText>(R.id.degreeName).text.toString()
            val instituteName = educationView.findViewById<EditText>(R.id.instituteName).text.toString()
            val passingYear = educationView.findViewById<EditText>(R.id.passingYear).text.toString()
            val location = educationView.findViewById<EditText>(R.id.Location).text.toString()
            val a = educationView.findViewById<CheckBox>(R.id.a)
            val b = educationView.findViewById<CheckBox>(R.id.b)
            val c = educationView.findViewById<CheckBox>(R.id.c)
            val d = educationView.findViewById<CheckBox>(R.id.d)

            var grade : String
            if (a.isChecked){
                grade = "A"
            }else if (b.isChecked){
                grade = "B"
            }else if (b.isChecked){
                grade = "C"
            }else{
                grade = "D"
            }
           value = db.insertEducationDetails(Resume_id,degreeName,location,instituteName,passingYear,grade)
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java).apply {
                putExtra("resume_id",Resume_id)
            }
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addEducation(){
        val educationDetailsView : View = LayoutInflater.from(this). inflate(R.layout.education_details,layoutcontainer,false)


        val delete : Button = educationDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontainer.removeView(educationDetailsView)
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

        layoutcontainer.addView(educationDetailsView)
        save.visibility=View.VISIBLE
    }
}