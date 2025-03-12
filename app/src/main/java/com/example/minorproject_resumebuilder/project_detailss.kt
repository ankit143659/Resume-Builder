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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProjectDetailsActivity : AppCompatActivity() {

    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layout: LinearLayout
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detailss)

        layout = findViewById(R.id.layoutContainer)
        addLayout = findViewById(R.id.addProjects)
        save = findViewById(R.id.savebtn)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid

        if (userId != null) {
            loadData()
        }

        if (layout.childCount != 0) {
            save.visibility = View.VISIBLE
        } else {
            save.visibility = View.GONE
        }

        addLayout.setOnClickListener {
            addProject()
        }

        save.setOnClickListener {
            saveData()
        }
    }

    private fun addProject() {
        val newProjectView = LayoutInflater.from(this).inflate(R.layout.project, layout, false)
        val delete: Button = newProjectView.findViewById(R.id.delete)

        delete.setOnClickListener {
            showDeleteDialog(newProjectView, null)
        }

        layout.addView(newProjectView)
        save.visibility = View.VISIBLE
    }

    private fun saveData() {
        if (userId == null) return

        val projectsCollection = firestore.collection("users").document(userId!!).collection("projects")

        var isSuccess = true
        for (i in 0 until layout.childCount) {
            val projectView = layout.getChildAt(i)
            val projectName = projectView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = projectView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = projectView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = projectView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = projectView.findViewById<EditText>(R.id.endDate).text.toString()

            val projectId = projectView.tag as? String ?: projectsCollection.document().id

            val projectData = mapOf(
                "projectName" to projectName,
                "role" to role,
                "projectUrl" to projectUrl,
                "description" to description,
                "startDate" to startDate,
                "endDate" to endDate
            )

            projectsCollection.document(projectId)
                .set(projectData)
                .addOnFailureListener {
                    isSuccess = false
                }
        }

        if (isSuccess) {
            Toast.makeText(this, "Successfully saved projects", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java))
            finish()
        } else {
            Toast.makeText(this, "Failed to save projects", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (userId == null) return

        firestore.collection("users").document(userId!!)
            .collection("projects")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    loadProjectData(
                        document.id,
                        document.getString("projectName") ?: "",
                        document.getString("description") ?: "",
                        document.getString("startDate") ?: "",
                        document.getString("endDate") ?: "",
                        document.getString("role") ?: "",
                        document.getString("projectUrl") ?: ""
                    )
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load projects", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProjectData(
        projectId: String,
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

        newProjectView.tag = projectId
        layout.addView(newProjectView)

        val delete: Button = newProjectView.findViewById(R.id.delete)
        delete.setOnClickListener {
            showDeleteDialog(newProjectView, projectId)
        }
    }

    private fun showDeleteDialog(projectView: View, projectId: String?) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
        dialog.setView(dialogView)

        val yes: Button = dialogView.findViewById(R.id.yes)
        val no: Button = dialogView.findViewById(R.id.no)

        val alertBox = dialog.create()

        yes.setOnClickListener {
            if (projectId != null && userId != null) {
                firestore.collection("users").document(userId!!)
                    .collection("projects").document(projectId)
                    .delete()
                    .addOnSuccessListener {
                        layout.removeView(projectView)
                        if (layout.childCount == 0) save.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete project", Toast.LENGTH_SHORT).show()
                    }
            } else {
                layout.removeView(projectView)
                if (layout.childCount == 0) save.visibility = View.GONE
            }
            alertBox.dismiss()
        }

        no.setOnClickListener {
            alertBox.dismiss()
        }

        alertBox.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Create_resume::class.java))
    }
}
