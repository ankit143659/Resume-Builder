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
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class ExperienceDetails : AppCompatActivity() {

    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layoutContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private var resumeId: Long? = null
    private lateinit var share: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_experience_detailss)
        
        share = SharePrefrence(this)
        resumeId = share.getResumeId()
        db = SQLiteHelper(this)
        
        addLayout = findViewById(R.id.addexperience)
        layoutContainer = findViewById(R.id.layoutContainer)
        save = findViewById(R.id.savebtn)

        // Load existing experience details if available
        val experienceDetails = db.getAllExperienceDetails(resumeId)
        if (experienceDetails != null && experienceDetails.isNotEmpty()) {
            loadData(experienceDetails)
        }

        addLayout.setOnClickListener {
            addExperience()
        }

        save.setOnClickListener {
            saveDetails()
        }
    }

    private fun saveDetails() {
        var allSaved = true

        for (i in 0 until layoutContainer.childCount) {
            val experienceView = layoutContainer.getChildAt(i)
            val jobTitle = experienceView.findViewById<EditText>(R.id.jobTitle).text.toString()
            val companyName = experienceView.findViewById<EditText>(R.id.companyName).text.toString()
            val companyLocation = experienceView.findViewById<EditText>(R.id.comanyLocation).text.toString()
            val startDate = experienceView.findViewById<EditText>(R.id.startDate).text.toString()

            val saved = db.insertExperience(resumeId, jobTitle, companyName, companyLocation, startDate)
            if (!saved) {
                allSaved = false
            }
        }

        if (allSaved) {
            Toast.makeText(this, "Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", resumeId)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to save Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addExperience() {
        val experienceView = LayoutInflater.from(this).inflate(R.layout.experience_details, layoutContainer, false)

        val deleteButton: Button = experienceView.findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yesButton: Button = dialogView.findViewById(R.id.yes)
            val noButton: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yesButton.setOnClickListener {
                layoutContainer.removeView(experienceView)
                if (layoutContainer.childCount == 0) {
                    save.visibility = View.GONE
                }
                alertBox.dismiss()
            }

            noButton.setOnClickListener {
                alertBox.dismiss()
            }

            alertBox.show()
        }

        layoutContainer.addView(experienceView)
        save.visibility = View.VISIBLE
    }

    private fun loadData(experienceDetails: List<Experience>) {
        experienceDetails.forEach { exp ->
            loadExperienceData(exp.jobTitle, exp.companyName, exp.location, exp.yearsOfExperience)
        }
    }

    private fun loadExperienceData(title: String, name: String, location: String, year: String) {
        val experienceView = LayoutInflater.from(this).inflate(R.layout.experience_details, layoutContainer, false)

        val jobTitle = experienceView.findViewById<EditText>(R.id.jobTitle)
        val companyName = experienceView.findViewById<EditText>(R.id.companyName)
        val companyLocation = experienceView.findViewById<EditText>(R.id.comanyLocation)
        val startDate = experienceView.findViewById<EditText>(R.id.startDate)

        jobTitle.setText(title)
        companyName.setText(name)
        companyLocation.setText(location)
        startDate.setText(year)

        layoutContainer.addView(experienceView)
    }

    private fun updateData(id: Long, i: Int) {
        var updateSuccessful = true

        for (i in 0 until layoutContainer.childCount) {
            val experienceView = layoutContainer.getChildAt(i)
            val jobTitle = experienceView.findViewById<EditText>(R.id.jobTitle).text.toString()
            val companyName = experienceView.findViewById<EditText>(R.id.companyName).text.toString()
            val companyLocation = experienceView.findViewById<EditText>(R.id.comanyLocation).text.toString()
            val startDate = experienceView.findViewById<EditText>(R.id.startDate).text.toString()

            val updated = db.updateExperience(id, jobTitle, companyName, companyLocation, startDate)
            if (!updated) {
                updateSuccessful = false
            }
        }

        if (updateSuccessful) {
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to update Data", Toast.LENGTH_SHORT).show()
        }
    }
}
