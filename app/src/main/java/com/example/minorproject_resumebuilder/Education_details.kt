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
    private lateinit var layoutContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private var Resume_id: Long? = null
    private lateinit var share: SharePrefrence
    private lateinit var educationDetailsView: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_education_details)
        
        share = SharePrefrence(this)
        Resume_id = share.getResumeId()
        addLayout = findViewById(R.id.addEducation)
        layoutContainer = findViewById(R.id.layoutContainer)
        save = findViewById(R.id.savebtn)
        db = SQLiteHelper(this)
        educationDetailsView = LayoutInflater.from(this).inflate(R.layout.education_details, layoutContainer, false)

        val educationDetailsUpdate = db.getAllEducationDetails(Resume_id)

        if (educationDetailsUpdate != null) {
            loadDetails()
        }
        if(layoutContainer.childCount!=0){
            save.visibility=View.VISIBLE
        }

        addLayout.setOnClickListener {
            addEducation()
        }

        save.setOnClickListener {
            saveDetails()
        }
    }

    private fun loadDetails() {
        val educationDetailsUpdate = db.getAllEducationDetails(Resume_id)

        educationDetailsUpdate.forEach { education ->
            loadeducationDetails(education.education_id,education.Degree_name,education.passingYear, education.Institute_name, education.grade, education.Location)
        }


    }


    private fun loadeducationDetails(eduId : Long,degreename: String,PassingYear:String, institutename: String, Grade: String, Location: String) {
        educationDetailsView = LayoutInflater.from(this).inflate(R.layout.education_details, layoutContainer, false)
        val degreeName = educationDetailsView.findViewById<EditText>(R.id.degreeName)
        val instituteName = educationDetailsView.findViewById<EditText>(R.id.instituteName)
        val location = educationDetailsView.findViewById<EditText>(R.id.Location)
        val a = educationDetailsView.findViewById<CheckBox>(R.id.a)
        val passingYear = educationDetailsView.findViewById<EditText>(R.id.passingYear)
        val b = educationDetailsView.findViewById<CheckBox>(R.id.b)
        val c = educationDetailsView.findViewById<CheckBox>(R.id.c)
        val d = educationDetailsView.findViewById<CheckBox>(R.id.d)

        degreeName.setText(degreename)
        instituteName.setText(institutename)
        location.setText(Location)
        passingYear.setText(PassingYear)

        when (Grade) {
            "A" -> a.isChecked = true
            "B" -> b.isChecked = true
            "C" -> c.isChecked = true
            "D" -> d.isChecked = true
        }
        educationDetailsView.tag = eduId
        layoutContainer.addView(educationDetailsView)
    }

    private fun saveDetails() {
        var value = true
        for (i in 0 until layoutContainer.childCount) {
            val educationView = layoutContainer.getChildAt(i)
            val degreeName = educationView.findViewById<EditText>(R.id.degreeName).text.toString()
            val instituteName = educationView.findViewById<EditText>(R.id.instituteName).text.toString()
            val passingYear = educationView.findViewById<EditText>(R.id.passingYear).text.toString()
            val location = educationView.findViewById<EditText>(R.id.Location).text.toString()
            val a = educationView.findViewById<CheckBox>(R.id.a)
            val b = educationView.findViewById<CheckBox>(R.id.b)
            val c = educationView.findViewById<CheckBox>(R.id.c)
            val d = educationView.findViewById<CheckBox>(R.id.d)
            val eduId = educationView.tag as? Long

            val grade: String = when {
                a.isChecked -> "A"
                b.isChecked -> "B"
                c.isChecked -> "C"
                else -> "D"
            }
            val value2 = if (eduId!=null){
                db.updateEducationDetails(eduId,degreeName,instituteName,passingYear,grade,location)
            }else{
                db.insertEducationDetails(Resume_id, degreeName, instituteName, location, passingYear, grade)
            }
            if (!value2){
                value = false
            }


            if (!value) {
                Toast.makeText(this, "Failed to save Data", Toast.LENGTH_SHORT).show()
                return
            }
        }

        Toast.makeText(this, "Successfully filled Data", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Create_resume::class.java).apply {
            putExtra("resume_id", Resume_id)
        }
        startActivity(intent)
        finish()
    }

    @SuppressLint("MissingInflatedId")
    private fun addEducation() {
        educationDetailsView = LayoutInflater.from(this).inflate(R.layout.education_details, layoutContainer, false)
        val delete: Button = educationDetailsView.findViewById(R.id.delete)

        delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yes: Button = dialogView.findViewById(R.id.yes)
            val no: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener {
                layoutContainer.removeView(educationDetailsView)
                if (layoutContainer.childCount == 0) {
                    save.visibility = View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener {
                alertBox.dismiss()
            }

            alertBox.show()
        }

        layoutContainer.addView(educationDetailsView)
        save.visibility = View.VISIBLE
    }
}
