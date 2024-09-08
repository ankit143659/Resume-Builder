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
import androidx.appcompat.app.AppCompatActivity
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class project_detailss : AppCompatActivity() {

    // Declare variables for UI components and database helper
    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layout: LinearLayout
    private lateinit var db: SQLiteHelper
    private var resumeId: Long? = null
    private lateinit var share: SharePrefrence

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detailss)

        // Initialize SharedPreference, Database, and UI components
        share = SharePrefrence(this)
        resumeId = share.getResumeId()
        db = SQLiteHelper(this)
        layout = findViewById(R.id.layoutContainer)
        addLayout = findViewById(R.id.addProjects)
        save = findViewById(R.id.savebtn)

        // Load existing project details if available
        val projectDetails = db.getAllProjectDetails(resumeId)
        if (projectDetails != null && projectDetails.isNotEmpty()) {
            loadData()
        }

        // Set listeners for add and save buttons
        addLayout.setOnClickListener {
            addProject()
        }

        save.setOnClickListener {
            saveData()
        }
    }

    // Method to add a new project layout dynamically
    private fun addProject() {
        val newProjectView = LayoutInflater.from(this).inflate(R.layout.project, layout, false)
        val delete: Button = newProjectView.findViewById(R.id.delete)

        // Set listener for delete button in each project view
        delete.setOnClickListener {
            val dialog = android.app.AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yes: Button = dialogView.findViewById(R.id.yes)
            val no: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener {
                layout.removeView(newProjectView)
                if (layout.childCount == 0) {
                    save.visibility = View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener {
                alertBox.dismiss()
            }

            alertBox.show()
        }

        layout.addView(newProjectView)
        save.visibility = View.VISIBLE
    }

    // Method to save the project details to the database
    private fun saveData() {
        var isSuccess = false
        for (i in 0 until layout.childCount) {
            val projectView = layout.getChildAt(i)
            val projectName = projectView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = projectView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = projectView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = projectView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = projectView.findViewById<EditText>(R.id.endDate).text.toString()

            isSuccess = db.insertProject(resumeId, projectName, description, projectUrl, startDate, endDate, role)
        }

        if (isSuccess) {
            Toast.makeText(this, "Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", resumeId)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to fill Data", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to load existing project data from the database
    private fun loadData() {
        val projectDetails = db.getAllProjectDetails(resumeId)
        projectDetails?.forEach { project ->
            loadProjectData(
                project.projectName,
                project.projectDescription,
                project.startDate,
                project.endDate,
                project.userRole,
                project.projectUrl
            )
        }

        save.setOnClickListener {
            var i = 0
            projectDetails?.forEach { project ->
                updateData(project.project_id, i)
                i++
            }
        }
    }

    // Method to update existing project data in the database
    private fun updateData(id: Long, index: Int) {
        val projectViewToUpdate = layout.getChildAt(index)
        var isSuccess = false

        val projectName = projectViewToUpdate.findViewById<EditText>(R.id.projectName).text.toString()
        val role = projectViewToUpdate.findViewById<EditText>(R.id.projectRole).text.toString()
        val projectUrl = projectViewToUpdate.findViewById<EditText>(R.id.projectUrl).text.toString()
        val description = projectViewToUpdate.findViewById<EditText>(R.id.projectDescription).text.toString()
        val startDate = projectViewToUpdate.findViewById<EditText>(R.id.startDate).text.toString()
        val endDate = projectViewToUpdate.findViewById<EditText>(R.id.endDate).text.toString()

        isSuccess = db.updateProject(id, projectName, description, projectUrl, startDate, endDate, role)

        if (isSuccess) {
            Toast.makeText(this, "Successfully updated Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", resumeId)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to update Data", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to load project data into UI components
    private fun loadProjectData(
        name: String,
        description: String,
        start: String,
        end: String,
        role: String,
        url: String
    ) {
        val newProjectView = LayoutInflater.from(this).inflate(R.layout.project, layout, false)
        val projectName = newProjectView.findViewById<EditText>(R.id.projectName)
        val projectRole = newProjectView.findViewById<EditText>(R.id.projectRole)
        val projectUrl = newProjectView.findViewById<EditText>(R.id.projectUrl)
        val projectDescription = newProjectView.findViewById<EditText>(R.id.projectDescription)
        val startDate = newProjectView.findViewById<EditText>(R.id.startDate)
        val endDate = newProjectView.findViewById<EditText>(R.id.endDate)

        projectName.setText(name)
        projectRole.setText(role)
        projectUrl.setText(url)
        projectDescription.setText(description)
        startDate.setText(start)
        endDate.setText(end)

        layout.addView(newProjectView)
    }
}
