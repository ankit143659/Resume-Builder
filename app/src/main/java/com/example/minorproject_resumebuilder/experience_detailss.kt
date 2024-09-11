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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class experience_detailss : AppCompatActivity() {

    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontain : LinearLayout
    private lateinit var db : SQLiteHelper
    var Resume_id : Long? = null
    private lateinit var share : SharePrefrence
    private lateinit var experienceDetailsView : View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_experience_detailss)
        share = SharePrefrence(this)
        Resume_id = share.getResumeId()
        db = SQLiteHelper(this)
        addLayout= findViewById(R.id.addexperience)
        layoutcontain = findViewById(R.id.layoutContainer)
        save= findViewById(R.id.savebtn)

        experienceDetailsView = LayoutInflater.from(this). inflate(R.layout.experience_details,layoutcontain,false)

        val experienceDetails = db.getAllExperienceDetails(Resume_id)

        if(experienceDetails!=null){
            loadData()
        }else{
            save.visibility = View.GONE
        }
        if(layoutcontain.childCount!=0){
            save.visibility=View.VISIBLE
        }

        addLayout.setOnClickListener{
            addExperience()
        }

        save.setOnClickListener{
            saveDetails()
        }

    }

    private fun saveDetails() {
        var value = true
        for (i in 0 until layoutcontain.childCount){
            val experienceView = layoutcontain.getChildAt(i)
            val jobTitle = experienceView.findViewById<EditText>(R.id.jobTitle).text.toString()
            val companyName = experienceView.findViewById<EditText>(R.id.companyName).text.toString()
            val companyLocation = experienceView.findViewById<EditText>(R.id.comanyLocation).text.toString()
            val startDate = experienceView.findViewById<EditText>(R.id.startDate).text.toString()
            val expId = experienceView.tag as?Long

            val value2 =if (expId!=null){
                db.updateExperience(expId,companyName,companyLocation,startDate)
            }else{
               db.insertExperience(Resume_id,jobTitle,companyName,companyLocation,startDate)

            }
            if (!value2){
                value = false
            }
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java).apply {
                putExtra("resume_id",Resume_id)
            }
            startActivity(intent)
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun addExperience(){
        experienceDetailsView = LayoutInflater.from(this). inflate(R.layout.experience_details,layoutcontain,false)
        val delete : Button = experienceDetailsView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontain.removeView(experienceDetailsView)
                if(layoutcontain.childCount==0){
                    save.visibility= View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layoutcontain.addView(experienceDetailsView)
        save.visibility= View.VISIBLE
    }

    private fun loadData(){
        val experienceDetails = db.getAllExperienceDetails(Resume_id)
        experienceDetails.forEach{exp->
            loadExperiencedata(exp.jobTitle,exp.companyName,exp.location,exp.yearsOfExperience,exp.experience_id)
        }

    }

    private fun loadExperiencedata(title:String,name:String,location:String,year:String,expId : Long){
        experienceDetailsView = LayoutInflater.from(this). inflate(R.layout.experience_details,layoutcontain,false)
        val jobTitle = experienceDetailsView.findViewById<EditText>(R.id.jobTitle)
        val companyName = experienceDetailsView.findViewById<EditText>(R.id.companyName)
        val companyLocation = experienceDetailsView.findViewById<EditText>(R.id.comanyLocation)
        val startDate = experienceDetailsView.findViewById<EditText>(R.id.startDate)

        jobTitle.setText(title)
        companyName.setText(name)
        companyLocation.setText(location)
        startDate.setText(year)

        experienceDetailsView.tag = expId
        layoutcontain.addView(experienceDetailsView)
        val delete: Button = experienceDetailsView.findViewById(R.id.delete)
        val layoutId = experienceDetailsView.tag as?Long
        delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yes: Button = dialogView.findViewById(R.id.yes)
            val no: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener {
                if (layoutId!=null){
                    db.deleteExperience(layoutId)
                    layoutcontain.removeView(experienceDetailsView)
                }

                if (layoutcontain.childCount == 0) {
                    save.visibility = View.GONE
                }
                alertBox.dismiss()
                val intent = intent
                startActivity(intent)
            }

            no.setOnClickListener {
                alertBox.dismiss()
            }

            alertBox.show()
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,Create_resume::class.java)
        startActivity(intent)
    }

}