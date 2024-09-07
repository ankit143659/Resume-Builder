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
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Education_details : AppCompatActivity() {
    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layoutcontainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private var resumeId: Long? = null
    private lateinit var share: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_education_details)

        // Initialize variables
        share = SharePrefrence(this)
        resumeId = share.getResumeId()
        addLayout = findViewById(R.id.addEducation)
        layoutcontainer = findViewById(R.id.layoutContainer)
        save = findViewById(R.id.savebtn)
        db = SQLiteHelper(this)

        // Load existing education details if available
        val educationDetailsUpdate = db.getAllEducationDetails(resumeId)
        if (educationDetailsUpdate != null && educationDetailsUpdate.isNotEmpty()) {
            loadDetails(educationDetailsUpdate)
        }

        // Set onClickListeners
        addLayout.setOnClickListener {
            addEducation()
        }

        save.setOnClickListener {
            saveDetails(educationDetailsUpdate)
        }
    }

    private fun loadDetails(educationDetailsUpdate: List<Education>) {
        educationDetailsUpdate.forEach { education ->
            loadEducationDetails(
                education.Degree_name,
                education.Institute_name,
                education.grade,
                education.Location
            )
        }
    }

    private fun loadEducationDetails(degreeName: String, instituteName: String, grade: String, location: String) {
        val educationDetailsView = LayoutInflater.from(this).inflate(R.layout.education_details, layoutcontainer, false)

        val degreeNameField = educationDetailsView.findViewById<EditText>(R.id.degreeName)
        val instituteNameField = educationDetailsView.findViewById<EditText>(R.id.instituteName)
        val locationField = educationDetailsView.findViewById<EditText>(R.id.Location)
        val a = educationDetailsView.findViewById<CheckBox>(R.id.a)
        val b = educationDetailsView.findViewById<CheckBox>(R.id.b)
        val c = educationDetailsView.findViewById<CheckBox>(R.id.c)
        val d = educationDetailsView.findViewById<CheckBox>(R.id.d)

        degreeNameField.setText(degreeName)
        instituteNameField.setText(instituteName)
        locationField.setText(location)

        when (grade) {
            "A" -> a.isChecked = true
            "B" -> b.isChecked = true
            "C" -> c.isChecked = true
            else -> d.isChecked = true
        }

        layoutcontainer.addView(educationDetailsView)
    }

    private fun updateDetails(id: Long, educationView: View): Boolean {
        val degreeName = educationView.findViewById<EditText>(R.id.degreeName).text.toString()
        val instituteName = educationView.findViewById<EditText>(R.id.instituteName).text.toString()
        val passingYear = educationView.findViewById<EditText>(R.id.passingYear).text.toString()
        val location = educationView.findViewById<EditText>(R.id.Location).text.toString()

        val grade = when {
            educationView.findViewById<CheckBox>(R.id.a).isChecked -> "A"
            educationView.findViewById<CheckBox>(R.id.b).isChecked -> "B"
            educationView.findViewById<CheckBox>(R.id.c).isChecked -> "C"
            else -> "D"
        }

        return db.updateEducationDetails(id, degreeName, instituteName, passingYear, grade, location)
    }

    private fun saveDetails(educationDetailsUpdate: List<Education>?) {
        var value = true

        // Update existing education details
        educationDetailsUpdate?.forEachIndexed { index, education ->
            val educationView = layoutcontainer.getChildAt(index)
            if (!updateDetails(education.education_id, educationView)) {
                value = false
            }
        }

        // Insert new education details
        for (i in educationDetailsUpdate?.size ?: 0 until layoutcontainer.childCount) {
            val educationView = layoutcontainer.getChildAt(i)
            if (!saveNewEducationDetails(educationView)) {
                value = false
            }
        }

        if (value) {
            Toast.makeText(this, "Successfully saved data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", resumeId)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNewEducationDetails(educationView: View): Boolean {
        val degreeName = educationView.findViewById<EditText>(R.id.degreeName).text.toString()
        val instituteName = educationView.findViewById<EditText>(R.id.instituteName).text.toString()
        val passingYear = educationView.findViewById<EditText>(R.id.passingYear).text.toString()
        val location = educationView.findViewById<EditText>(R.id.Location).text.toString()

        val grade = when {
            educationView.findViewById<CheckBox>(R.id.a).isChecked -> "A"
            educationView.findViewById<CheckBox>(R.id.b).isChecked -> "B"
            educationView.findViewById<CheckBox>(R.id.c).isChecked -> "C"
            else -> "D"
        }

        return db.insertEducationDetails(resumeId, degreeName, location, instituteName, passingYear, grade)
    }

    @SuppressLint("MissingInflatedId")
    fun addEducation() {
        val educationDetailsView = LayoutInflater.from(this).inflate(R.layout.education_details, layoutcontainer, false)

        val delete: Button = educationDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yes: Button = dialogView.findViewById(R.id.yes)
            val no: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener {
                layoutcontainer.removeView(educationDetailsView)
                if (layoutcontainer.childCount == 0) {
                    save.visibility = View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener {
                alertBox.dismiss()
            }

            alertBox.show()
        }

        layoutcontainer.addView(educationDetailsView)
        save.visibility = View.VISIBLE
    }
}
