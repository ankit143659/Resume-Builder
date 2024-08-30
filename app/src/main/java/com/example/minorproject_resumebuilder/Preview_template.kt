package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Preview_template : AppCompatActivity() {

    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var buttonContainer : LinearLayout
    private lateinit var db : SQLiteHelper
    private lateinit var share : SharePrefrence
    var resume_id : Int? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview_template)
        db = SQLiteHelper(this)
        val Resume_id = intent.getStringExtra("resume_id")
        resume_id = Resume_id?.toInt()

        val intent = Intent(getIntent())
        val resumeName :String = intent.getStringExtra("value").toString()

        layoutcontainer = findViewById(R.id.layoutcontainer)
        buttonContainer = findViewById(R.id.buttonContainer)


        if (resumeName =="medical_1"){
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.medical_1,layoutcontainer,false)

            val personalDetails = resume_preview.findViewById<TextView>(R.id.personalDetails)
            val name = resume_preview.findViewById<TextView>(R.id.name)

            val personalDetail = db.getPersonalDetails(resume_id)
            personalDetail?.let {
                personalDetails.text = " \nPhone no: ${it.phone}\nEmail id: ${it.email}\n" +
                        "Nationality: ${it.nationality}\nGender: ${it.gender}\nDate of Birth: ${it.dateOfBirth}\nProfile Image: ${it.profileImage}"
                name.text = "Name: ${it.fname} '' ${it.lname}"
            }

            val EducationDetails = db.getAllEducationDetails(resume_id)
            val educationTextView: TextView = findViewById(R.id.educationDetails)
            educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
                "Degree Name: ${it.Degree_name}\n, Institute Name : ${it.Institute_name}\n,Passing Year: ${it.passingYear}\n, Grade: ${it.grade} "
            }


            val skills = db.getAllSkills(resume_id)
            val skillsTextView: TextView = findViewById(R.id.skillDetails)
            skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
                "Skill Name: ${it.skillName}, Strength: ${it.strength}"
            }

            val experiences = db.getAllExperienceDetails(resume_id)
            val experienceTextView: TextView = findViewById(R.id.experienceDetails)
            experienceTextView.text = experiences.joinToString(separator = "\n") {
                "Company Name: ${it.companyName}, Location: ${it.location}, Years of Experience: ${it.yearsOfExperience}"
            }

            val projects = db.getAllProjectDetails(resume_id)
            val projectsTextView: TextView = findViewById(R.id.projectDetails)
            projectsTextView.text = projects.joinToString(separator = "\n") {
                "Project Name: ${it.projectName}, Project Url : ${it.projectUrl}, Start Date: ${it.startDate}, End Date: ${it.endDate}, Role: ${it.userRole},Description: ${it.projectDescription}"
            }


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