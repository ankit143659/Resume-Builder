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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class ProjectDetails : AppCompatActivity() {

    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layoutContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private var resumeId: Long? = null
    private lateinit var share: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detailss)

        share = SharePrefrence(this)
        resumeId = share.getResumeId()
        db = SQLiteHelper(this)

        addLayout = findViewById(R.id.addProjects)
        layoutContainer = findViewById(R.id.layoutContainer)
        save = findViewById(R.id.savebtn)

        // Load existing project details if available
        val projectDetails = db.getAllProjectDetails(resumeId)
        if (projectDetails != null && projectDetails.isNotEmpty()) {
            loadData(projectDetails)
        }

        addLayout.setOnClickListener {
            addProject()
        }

        save.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        var allSaved = true

        for (i in 0 until layoutContainer.childCount) {
            val projectView = layoutContainer.getChildAt(i)
            val projectName = projectView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = projectView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = projectView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = projectView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = projectView.findViewById<EditText>(R.id.endDate).text.toString()

            val saved = db.insertProject(resumeId, projectName, description, projectUrl, startDate, endDate, role)
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
            Toast.makeText(this@ProjectDetails, "Failed to save Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addProject() {
        val projectView = LayoutInflater.from(this).inflate(R.layout.project, layoutContainer, false)

        val deleteButton: Button = projectView.findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yesButton: Button = dialogView.findViewById(R.id.yes)
            val noButton: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yesButton.setOnClickListener {
                layoutContainer.removeView(projectView)
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

        layoutContainer.addView(projectView)
        save.visibility = View.VISIBLE
    }

    private fun loadData(projectDetails: List<Project>) {
        projectDetails.forEach { project ->
            loadProjectData(
                project.projectName,
                project.projectDescription,
                project.startDate,
                project.endDate,
                project.userRole,
                project.projectUrl
            )
        }
    }

    private fun loadProjectData(name: String, description: String, start: String, end: String, role: String, url: String) {
        val projectView = LayoutInflater.from(this).inflate(R.layout.project, layoutContainer, false)

        val projectName = projectView.findViewById<EditText>(R.id.projectName)
        val projectRole = projectView.findViewById<EditText>(R.id.projectRole)
        val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl)
        val projectDescription = projectView.findViewById<EditText>(R.id.projectDescription)
        val startDate = projectView.findViewById<EditText>(R.id.startDate)
        val endDate = projectView.findViewById<EditText>(R.id.endDate)

        projectName.setText(name)
        projectRole.setText(role)
        projectUrl.setText(url)
        projectDescription.setText(description)
        startDate.setText(start)
        endDate.setText(end)

        layoutContainer.addView(projectView)
    }

    private fun updateData(id: Long, i: Int) {
        var allUpdated = true

        for (i in 0 until layoutContainer.childCount) {
            val projectView = layoutContainer.getChildAt(i)
            val projectName = projectView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = projectView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = projectView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = projectView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = projectView.findViewById<EditText>(R.id.endDate).text.toString()

            val updated = db.updateProject(id, projectName, description, projectUrl, startDate, endDate, role)
            if (!updated) {
                allUpdated = false
            }
        }

        if (allUpdated) {
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", resumeId)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@ProjectDetails, "Failed to update Data", Toast.LENGTH_SHORT).show()
        }
    }
}
