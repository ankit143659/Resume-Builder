package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.BitSet

class Preview_template : AppCompatActivity() {

    private lateinit var save: Button
    private lateinit var download: Button
    private lateinit var layoutcontainer: LinearLayout
    private lateinit var buttonContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private lateinit var resume_Name: String
    private lateinit var resume_preview : View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_template)

        share = SharePrefrence(this)
        db = SQLiteHelper(this)

        val Resume_id = share.getResumeId()
        val resumeName: String? = share.getTemplateName()

        layoutcontainer = findViewById(R.id.layoutcontainer)
        buttonContainer = findViewById(R.id.buttonContainer)

        Log.d("Preview_template", "Resume Name: $resumeName, Resume ID: $Resume_id")

        if (resumeName != null && Resume_id != null) {
            loadResumePreview(resumeName, Resume_id)
        } else {
            Toast.makeText(this, "Failed to load resume. Invalid resume ID or name.", Toast.LENGTH_SHORT).show()
            buttonContainer.visibility = View.GONE
        }

        if (layoutcontainer.childCount != 0) {
            save = findViewById(R.id.Save)
            download = findViewById(R.id.Download)
            buttonContainer.visibility = View.VISIBLE
            setupButtonListeners(Resume_id)
        }
    }

    private fun loadResumePreview(resumeName: String, Resume_id: Long) {
        resume_Name = resumeName
        layoutcontainer.visibility = View.VISIBLE


         resume_preview = when (resumeName) {
            "medical_1" -> LayoutInflater.from(this).inflate(R.layout.medical_1, layoutcontainer, false)
            "engineering_1" -> LayoutInflater.from(this).inflate(R.layout.engineering_1, layoutcontainer, false)
            "basic_1" -> LayoutInflater.from(this).inflate(R.layout.basic_1, layoutcontainer, false)
            "basic_3" -> LayoutInflater.from(this).inflate(R.layout.basic_2, layoutcontainer, false)
            else -> {
                layoutcontainer.visibility = View.GONE
                buttonContainer.visibility = View.GONE
                return
            }
        }

        val personalDetail = db.getPersonalDetails(Resume_id)
        personalDetail?.let {
            resume_preview.findViewById<TextView>(R.id.personalDetails)?.text = """
                Phone no: ${it.phone}
                Email id: ${it.email}
                Nationality: ${it.nationality}
                Gender: ${it.gender}
                Date of Birth: ${it.dateOfBirth}
            """.trimIndent()

            resume_preview.findViewById<TextView>(R.id.name)?.text = "Name: ${it.fname} ${it.lname}"

            val imageView = resume_preview.findViewById<ImageView>(R.id.profile_image)
            val imageUri = Uri.parse(it.profileImage)
            imageView?.setImageURI(imageUri)
        }

        val EducationDetails = db.getAllEducationDetails(Resume_id)
        val educationTextView: TextView = resume_preview.findViewById(R.id.educationDetails)
        educationTextView.text = EducationDetails.joinToString(separator = "\n\n\n") {
            "Degree Name: ${it.Degree_name}\nInstitute Name: ${it.Institute_name}\nPassing Year: ${it.passingYear}\nGrade: ${it.grade}"
        }

        val skills = db.getAllSkills(Resume_id)
        val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
        skillsTextView.text = skills.joinToString(separator = "\n\n\n") {
            "Skill Name: ${it.skillName}, Strength: ${it.strength}"
        }

        val experiences = db.getAllExperienceDetails(Resume_id)
        val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
        experienceTextView.text = experiences.joinToString(separator = "\n\n\n") {
            "Job Title: ${it.jobTitle}\nCompany Name: ${it.companyName}\nLocation: ${it.location}\nYears of Experience: ${it.yearsOfExperience}"
        }

        val projects = db.getAllProjectDetails(Resume_id)
        val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
        projectsTextView.text = projects.joinToString(separator = "\n\n\n") {
            "Project Name: ${it.projectName}\nProject URL: ${it.projectUrl}\nStart Date: ${it.startDate}\nEnd Date: ${it.endDate}\nRole: ${it.userRole}\nDescription: ${it.projectDescription}"
        }

        layoutcontainer.addView(resume_preview)
    }
    private fun setupButtonListeners(Resume_id: Long) {
        save.setOnClickListener {
            var value = true
            if (share.checkUpdateMode()){
                db.updateResumeTemplateName(Resume_id,resume_Name)
            }else{
                 value = db.storeResumeName(Resume_id, resume_Name)
            }

            if (value) {
                Toast.makeText(this@Preview_template, "Resume saved successfully", Toast.LENGTH_SHORT).show()
                share.storeUpdateMode(false)
                val intent = Intent(this@Preview_template, HomePage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@Preview_template, "Resume save failed", Toast.LENGTH_SHORT).show()
            }
        }

        download.setOnClickListener {
                val bitMap = getbitMap(resume_preview)
            saveBitMaptoFile(bitMap,"My_resume.png")
                Toast.makeText(this@Preview_template, "Resume downloaded successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Preview_template, HomePage::class.java)
                startActivity(intent)
                finish()


        }
    }

    private fun saveBitMaptoFile(bitMap: Bitmap, fileName: String) {
        val file = File(Environment.getExternalStorageDirectory(),fileName)
        try{
            FileOutputStream(file).use { out ->
                bitMap.compress(Bitmap.CompressFormat.PNG,100,out)
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

    private fun getbitMap(resumePreview: View): Bitmap {
        val bitmap = Bitmap.createBitmap(resumePreview.width,resumePreview.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        resumePreview.draw(canvas)
        return bitmap
    }
}
