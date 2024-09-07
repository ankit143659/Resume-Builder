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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class project_detailss : AppCompatActivity() {
    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layout : LinearLayout
    private lateinit var db : SQLiteHelper
    var Resume_id : Long? = null
    private lateinit var share : SharePrefrence
    private lateinit var projectView : View


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_detailss)
        share= SharePrefrence(this)
        Resume_id = share.getResumeId()
        db = SQLiteHelper(this)
        projectView = LayoutInflater.from(this). inflate(R.layout.project,layout,false)
        addLayout= findViewById(R.id.addProjects)
        layout = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)

        val projectdetails = db.getAllProjectDetails(Resume_id)
        if (projectdetails!=null){
            loadData()
        }

        addLayout.setOnClickListener{
            addEducation()
        }

        save.setOnClickListener{
            saveData()
        }

    }

    private fun saveData() {
        var value : Boolean = false
        for (i in 0 until layout.childCount){
            val educationView = layout.getChildAt(i)
            val projectName = educationView.findViewById<EditText>(R.id.projectName).text.toString()
            val role = educationView.findViewById<EditText>(R.id.projectRole).text.toString()
            val projectUrl = educationView.findViewById<EditText>(R.id.projectUrl).text.toString()
            val description = educationView.findViewById<EditText>(R.id.projectDescription).text.toString()
            val startDate = educationView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = educationView.findViewById<EditText>(R.id.endDate).text.toString()

            value = db.insertProject(Resume_id,projectName,description,projectUrl,startDate,endDate,role)
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java).apply {
                putExtra("resume_id",Resume_id)
            }
            startActivity(intent)
        }else{
            Toast.makeText(this@project_detailss,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addEducation(){
        projectView = LayoutInflater.from(this). inflate(R.layout.project,layout,false)
        val delete : Button = projectView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = android.app.AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layout.removeView(projectView)
                if(layout.childCount==0){
                    save.visibility= View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layout.addView(projectView)
        save.visibility= View.VISIBLE
    }

    fun loadData(){
        val projectdetails = db.getAllProjectDetails(Resume_id)
        projectdetails.forEach{project->
            loadProjectData(project.projectName,
                project.projectDescription,
                project.startDate,
                project.endDate,
                project.userRole,
                project.projectUrl
            )
        }
        save.setOnClickListener{
            var i =1
            projectdetails.forEach{project->
                updateData(project.project_id,i)
                i++
            }
        }
    }

    private fun updateData(id:Long,i:Int) {
        projectView = LayoutInflater.from(this). inflate(R.layout.project,layout,false)
        var value : Boolean = false
        val ProjectView = layout.getChildAt(i)
        val projectName = ProjectView.findViewById<EditText>(R.id.projectName).text.toString()
        val role = ProjectView.findViewById<EditText>(R.id.projectRole).text.toString()
        val projectUrl = ProjectView.findViewById<EditText>(R.id.projectUrl).text.toString()
        val description = ProjectView.findViewById<EditText>(R.id.projectDescription).text.toString()
        val startDate = ProjectView.findViewById<EditText>(R.id.startDate).text.toString()
        val endDate = ProjectView.findViewById<EditText>(R.id.endDate).text.toString()
        value = db.insertProject(id,projectName,description,projectUrl,startDate,endDate,role)

        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java).apply {
                putExtra("resume_id",Resume_id)
            }
            startActivity(intent)
        }else{
            Toast.makeText(this@project_detailss,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadProjectData(name:String,Description:String,start:String,end:String,Role:String,url:String){
        projectView = LayoutInflater.from(this). inflate(R.layout.project,layout,false)
        val projectName = projectView.findViewById<EditText>(R.id.projectName)
        val role = projectView.findViewById<EditText>(R.id.projectRole)
        val projectUrl = projectView.findViewById<EditText>(R.id.projectUrl)
        val description = projectView.findViewById<EditText>(R.id.projectDescription)
        val startDate = projectView.findViewById<EditText>(R.id.startDate)
        val endDate = projectView.findViewById<EditText>(R.id.endDate)


        projectName.setText(name)
        role.setText(Role)
        projectUrl.setText(url)
        description.setText(Description)
        startDate.setText(start)
        endDate.setText(end)

        layout.addView(projectView)

    }


}