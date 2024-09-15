package com.example.minorproject_resumebuilder

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.properties.Delegates

class Preview_template : AppCompatActivity() {

    private lateinit var save: Button
    private lateinit var download: Button
    private lateinit var layoutcontainer: LinearLayout
    private lateinit var buttonContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private lateinit var resume_Name: String
    private lateinit var resume_preview: View
    private var Resume_id by Delegates.notNull<Long>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_template)

        share = SharePrefrence(this)
        db = SQLiteHelper(this)

        Resume_id = share.getResumeId()
        val resumeName: String? = share.getTemplateName()

        layoutcontainer = findViewById(R.id.layoutcontainer)
        buttonContainer = findViewById(R.id.buttonContainer)

        checkAndRequestPermissions()

        Log.d("Preview_template", "Resume Name: $resumeName, Resume ID: $Resume_id")

        if (resumeName != null) {
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

    @SuppressLint("SetTextI18n")
    private fun loadResumePreview(resumeName: String, Resume_id: Long) {
        resume_Name = resumeName
        layoutcontainer.visibility = View.VISIBLE

        resume_preview = when (resumeName) {
            "medical_1" -> LayoutInflater.from(this).inflate(R.layout.medical_1, layoutcontainer, false)
            "medical_2" -> LayoutInflater.from(this).inflate(R.layout.medical_2, layoutcontainer, false)
            "engineering_1" -> LayoutInflater.from(this).inflate(R.layout.engineering_1, layoutcontainer, false)
            "engineering_2" -> LayoutInflater.from(this).inflate(R.layout.engineering_2, layoutcontainer, false)
            "basic_1" -> LayoutInflater.from(this).inflate(R.layout.basic_1, layoutcontainer, false)
            "design_1" -> LayoutInflater.from(this).inflate(R.layout.design_1, layoutcontainer, false)
            "basic_2" -> LayoutInflater.from(this).inflate(R.layout.basic_2, layoutcontainer, false)
            "it_1" -> LayoutInflater.from(this).inflate(R.layout.it_1, layoutcontainer, false)
            "it_2" -> LayoutInflater.from(this).inflate(R.layout.it_2, layoutcontainer, false)
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

            resume_preview.findViewById<TextView>(R.id.name)?.text = "${it.fname} ${it.lname}"
            val imageView = resume_preview.findViewById<ImageView>(R.id.profile_image)

            // Handle the profile image loading
            val profileImagePath = it.profileImage
            if (!profileImagePath.isNullOrEmpty()) {
                val imageFile = File(profileImagePath)
                if (imageFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    imageView.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show()
            }
        }

        // Education Details
        val EducationDetails = db.getAllEducationDetails(Resume_id)
        val educationTextView: TextView = resume_preview.findViewById(R.id.educationalDeatils)
        educationTextView.text = EducationDetails.joinToString(separator = "\n\n") {
            "Degree Name: ${it.Degree_name}\nInstitute Name: ${it.Institute_name}\nPassing Year: ${it.passingYear}\nGrade: ${it.grade}"
        }

        // Skills Details
        val skills = db.getAllSkills(Resume_id)
        val skillsTextView: TextView = resume_preview.findViewById(R.id.skillDetails)
        skillsTextView.text = skills.joinToString(separator = "\n\n") {
            "Skill Name: ${it.skillName}, Strength: ${it.strength}"
        }

        // Experience Details
        val experiences = db.getAllExperienceDetails(Resume_id)
        val experienceTextView: TextView = resume_preview.findViewById(R.id.experienceDetails)
        experienceTextView.text = experiences.joinToString(separator = "\n\n") {
            "Job Title: ${it.jobTitle}\nCompany Name: ${it.companyName}\nLocation: ${it.location}\nYears of Experience: ${it.yearsOfExperience}"
        }

        // Project Details
        val projects = db.getAllProjectDetails(Resume_id)
        val projectsTextView: TextView = resume_preview.findViewById(R.id.projectDetails)
        projectsTextView.text = projects.joinToString(separator = "\n\n") {
            "Project Name: ${it.projectName}\nProject URL: ${it.projectUrl}\nStart Date: ${it.startDate}\nEnd Date: ${it.endDate}\nRole: ${it.userRole}\nDescription: ${it.projectDescription}"
        }

        layoutcontainer.addView(resume_preview)
    }

    private fun setupButtonListeners(Resume_id: Long) {
        save.setOnClickListener {
            var value = true
            if (share.checkUpdateMode()) {
                db.updateResumeTemplateName(Resume_id, resume_Name)
            } else {
                value = db.storeResumeName(Resume_id, resume_Name)
            }

            if (value) {
                Toast.makeText(this@Preview_template, "Resume saved successfully", Toast.LENGTH_SHORT).show()
                share.storeUpdateMode(false)
                val intent = Intent(this@Preview_template, HomePage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@Preview_template, "Resume save failed", Toast.LENGTH_SHORT).show()
            }
        }

        download.setOnClickListener {

            val storeDeatils = db.addDownloadedResume(share.getuser_id().toLong(),Resume_id,share.getResumeName(),share.getCreateDate())
            if (storeDeatils){
                val bitMap = getBitmapFromView(resume_preview)
                saveBitmapToFile(bitMap, "ResumeBuilder_Resume")
                val intent = Intent(this@Preview_template, HomePage::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@Preview_template, "Resume save failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String) {
        val outputStream: OutputStream?

        // Handle file saving based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
        } else {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "$fileName.png")
            outputStream = FileOutputStream(file)
        }

        try {
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                Toast.makeText(this, "Resume downloaded successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Basic_personal_details.READ_EXTERNAL_STORAGE_REQUEST
            )
        } else {
            // Permission is already granted, proceed with your code
            loadResumePreview(resume_Name, Resume_id)
        }
    }

}
