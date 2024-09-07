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
    private lateinit var download : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var buttonContainer : LinearLayout
    private lateinit var db : SQLiteHelper
    private lateinit var share : SharePrefrence
    private lateinit var resume_Name : String



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview_template)
        share = SharePrefrence(this)
        db = SQLiteHelper(this)
        val Resume_id = share.getResumeId()
        val resumeName :String? = share.getTemplateName()



        layoutcontainer = findViewById(R.id.layoutcontainer)
        buttonContainer = findViewById(R.id.buttonContainer)


        if (resumeName =="medical_1"){
            resume_Name = resumeName
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.medical_1,layoutcontainer,false)

            val personalDetails = resume_preview.findViewById<TextView>(R.id.personalDetails)
            val name = resume_preview.findViewById<TextView>(R.id.name)

            val personalDetail = db.getPersonalDetails(Resume_id)
            personalDetail?.let {
                personalDetails.text = " \nPhone no: ${it.phone}\nEmail id: ${it.email}\n" +
                        "Nationality: ${it.nationality}\nGender: ${it.gender}\nDate of Birth: ${it.dateOfBirth}\nProfile Image: ${it.profileImage}"
                name.text = "Name: ${it.fname} ${it.lname}"
            }

            val EducationDetails = db.getAllEducationDetails(Resume_id)
            val educationTextView: TextView = resume_preview.findViewById(R.id.educationDetails)
            educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
                "Degree Name: ${it.Degree_name}\n, Institute Name : ${it.Institute_name}\n,Passing Year: ${it.passingYear}\n, Grade: ${it.grade} "
            }


            val skills = db.getAllSkills(Resume_id)
            val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
            skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
                "Skill Name: ${it.skillName}\n, Strength: ${it.strength}"
            }

            val experiences = db.getAllExperienceDetails(Resume_id)
            val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
            experienceTextView.text = experiences.joinToString(separator = "\n\n\n") {
                "Job Title : ${it.jobTitle}\n"
                "Company Name: ${it.companyName}\n, Location: ${it.location}\n, Years of Experience: ${it.yearsOfExperience}"
            }

            val projects = db.getAllProjectDetails(Resume_id)
            val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
            projectsTextView.text = projects.joinToString(separator = "\n") {
                "Project Name: ${it.projectName}\n, Project Url : ${it.projectUrl}\n, Start Date: ${it.startDate}\n, End Date: ${it.endDate}\n, Role: ${it.userRole}\n,Description: ${it.projectDescription}"
            }


            layoutcontainer.addView(resume_preview)

        }else if (resumeName=="engineering_1"){
            resume_Name = resumeName
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.engineering_1,layoutcontainer,false)

            val personalDetails = resume_preview.findViewById<TextView>(R.id.personalDetails)
            val name = resume_preview.findViewById<TextView>(R.id.name)

            val personalDetail = db.getPersonalDetails(Resume_id)
            personalDetail?.let {
                personalDetails.text = " \nPhone no: ${it.phone}\nEmail id: ${it.email}\n" +
                        "Nationality: ${it.nationality}\nGender: ${it.gender}\nDate of Birth: ${it.dateOfBirth}\nProfile Image: ${it.profileImage}"
                name.text = "Name: ${it.fname} ${it.lname}"
            }

            val EducationDetails = db.getAllEducationDetails(Resume_id)
            val educationTextView: TextView = resume_preview.findViewById(R.id.educationalDeatils)
            educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
                "Degree Name: ${it.Degree_name}\n, Institute Name : ${it.Institute_name}\n,Passing Year: ${it.passingYear}\n, Grade: ${it.grade} "
            }


            val skills = db.getAllSkills(Resume_id)
            val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
            skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
                "Skill Name: ${it.skillName}, Strength: ${it.strength}"
            }

            val experiences = db.getAllExperienceDetails(Resume_id)
            val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
            experienceTextView.text = experiences.joinToString(separator = "\n") {
                "Company Name: ${it.companyName}, Location: ${it.location}, Years of Experience: ${it.yearsOfExperience}"
            }

            val projects = db.getAllProjectDetails(Resume_id)
            val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
            projectsTextView.text = projects.joinToString(separator = "\n") {
                "Project Name: ${it.projectName}, Project Url : ${it.projectUrl}, Start Date: ${it.startDate}, End Date: ${it.endDate}, Role: ${it.userRole},Description: ${it.projectDescription}"
            }


            layoutcontainer.addView(resume_preview)

        }else if (resumeName=="basic_3"){
            resume_Name = resumeName
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.basic_2,layoutcontainer,false)

            val personalDetails = resume_preview.findViewById<TextView>(R.id.personalDetails)
            val name = resume_preview.findViewById<TextView>(R.id.name)

            val personalDetail = db.getPersonalDetails(Resume_id)
            personalDetail?.let {
                personalDetails.text = " \nPhone no: ${it.phone}\nEmail id: ${it.email}\n" +
                        "Nationality: ${it.nationality}\nGender: ${it.gender}\nDate of Birth: ${it.dateOfBirth}\nProfile Image: ${it.profileImage}"
                name.text = "Name: ${it.fname} ${it.lname}"
            }

            val EducationDetails = db.getAllEducationDetails(Resume_id)
            val educationTextView: TextView = resume_preview.findViewById(R.id.educationalDeatils)
            educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
                "Degree Name: ${it.Degree_name}\n, Institute Name : ${it.Institute_name}\n,Passing Year: ${it.passingYear}\n, Grade: ${it.grade} "
            }


            val skills = db.getAllSkills(Resume_id)
            val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
            skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
                "Skill Name: ${it.skillName}, Strength: ${it.strength}"
            }

            val experiences = db.getAllExperienceDetails(Resume_id)
            val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
            experienceTextView.text = experiences.joinToString(separator = "\n") {
                "Company Name: ${it.companyName}, Location: ${it.location}, Years of Experience: ${it.yearsOfExperience}"
            }

            val projects = db.getAllProjectDetails(Resume_id)
            val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
            projectsTextView.text = projects.joinToString(separator = "\n") {
                "Project Name: ${it.projectName}, Project Url : ${it.projectUrl}, Start Date: ${it.startDate}, End Date: ${it.endDate}, Role: ${it.userRole},Description: ${it.projectDescription}"
            }


            layoutcontainer.addView(resume_preview)

        }else if (resumeName=="basic_1"){
            resume_Name = resumeName
            layoutcontainer.visibility=View.VISIBLE
            val resume_preview : View = LayoutInflater.from(this). inflate(R.layout.basic_1,layoutcontainer,false)

            val personalDetails = resume_preview.findViewById<TextView>(R.id.personalDetails)
            val name = resume_preview.findViewById<TextView>(R.id.name)

            val personalDetail = db.getPersonalDetails(Resume_id)
            personalDetail?.let {
                personalDetails.text = " \nPhone no: ${it.phone}\nEmail id: ${it.email}\n" +
                        "Nationality: ${it.nationality}\nGender: ${it.gender}\nDate of Birth: ${it.dateOfBirth}\nProfile Image: ${it.profileImage}"
                name.text = "Name: ${it.fname} ${it.lname}"
            }

            val EducationDetails = db.getAllEducationDetails(Resume_id)
            val educationTextView: TextView = resume_preview.findViewById(R.id.educationalDeatils)
            educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
                "Degree Name: ${it.Degree_name}\n, Institute Name : ${it.Institute_name}\n,Passing Year: ${it.passingYear}\n, Grade: ${it.grade} "
            }


            val skills = db.getAllSkills(Resume_id)
            val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
            skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
                "Skill Name: ${it.skillName}, Strength: ${it.strength}"
            }

            val experiences = db.getAllExperienceDetails(Resume_id)
            val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
            experienceTextView.text = experiences.joinToString(separator = "\n") {
                "Company Name: ${it.companyName}, Location: ${it.location}, Years of Experience: ${it.yearsOfExperience}"
            }

            val projects = db.getAllProjectDetails(Resume_id)
            val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
            projectsTextView.text = projects.joinToString(separator = "\n") {
                "Project Name: ${it.projectName}, Project Url : ${it.projectUrl}, Start Date: ${it.startDate}, End Date: ${it.endDate}, Role: ${it.userRole},Description: ${it.projectDescription}"
            }


            layoutcontainer.addView(resume_preview)

        }else{
            layoutcontainer.visibility=View.GONE
            buttonContainer.visibility=View.GONE
        }

        if (layoutcontainer.childCount!=0){
            save = findViewById(R.id.Save)
            download = findViewById(R.id.Download)
            buttonContainer.visibility=View.VISIBLE
            save.setOnClickListener{
                val value = db.storeResumeName(Resume_id,resume_Name)
                if(value){
                    Toast.makeText(this@Preview_template,"Resume Saved successfully",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Preview_template,HomePage::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@Preview_template,"Resume Saved failed",Toast.LENGTH_SHORT).show()
                }

            }
            download.setOnClickListener{
                Toast.makeText(this@Preview_template,"Resume Downloaded successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Preview_template,HomePage::class.java)
                startActivity(intent)
            }
        }


    }
}