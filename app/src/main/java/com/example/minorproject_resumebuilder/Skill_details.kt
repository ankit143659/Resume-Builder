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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Skill_details : AppCompatActivity() {

    private lateinit var addLayout : Button
    private lateinit var save : Button
    private lateinit var layoutcontainer : LinearLayout
    private lateinit var db : SQLiteHelper
    private lateinit var share: SharePrefrence
    var Resume_id : Long? = null
    private lateinit var skillView : View
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skill_details)
        share = SharePrefrence(this)
        addLayout=findViewById(R.id.addSkill)
        layoutcontainer=findViewById(R.id.layoutContainer)
        save=findViewById(R.id.savebtn)
        db = SQLiteHelper(this)
        Resume_id =share.getResumeId()
        skillView   =LayoutInflater.from(this). inflate(R.layout.skill_details,layoutcontainer,false)
        val skillDetails = db.getAllSkills(Resume_id)
        if (skillDetails!=null){
            loadDetails()
        }

        addLayout.setOnClickListener{
            addskills();
        }

        save.setOnClickListener{
            savedata()
        }
    }

    private fun loadDetails() {
        val skillDetails = db.getAllSkills(Resume_id)
        skillDetails.forEach{ skill->
            loadSkillDetails(skill.skillName,skill.strength)
        }
        save.setOnClickListener{
            var i =1
            skillDetails.forEach{skill->
                updateDeatils(skill.skill_id,i)
                i++
            }
        }

    }

    private fun updateDeatils(id:Long,i:Int) {
        var value = false
        for (i in 0 until layoutcontainer.childCount){
            val SkillView = layoutcontainer.getChildAt(i)
            val SkillName = SkillView.findViewById<EditText>(R.id.skillName).text.toString()
            val a = SkillView.findViewById<CheckBox>(R.id.begineer)
            val b = SkillView.findViewById<CheckBox>(R.id.intermediate)
            val c = SkillView.findViewById<CheckBox>(R.id.advance)

            var grade : String
            if (a.isChecked){
                grade = "Beginner"
            }else if (b.isChecked){
                grade = "Intermediate"
            }
            else{
                grade = "Advance"
            }
            value = db.updateSkill(id,SkillName,grade)
        }
        if (value){
            Toast.makeText(this,"Successfully Updated", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSkillDetails(skillname: String, strength: String) {
        skillView   =LayoutInflater.from(this). inflate(R.layout.skill_details,layoutcontainer,false)
        val skillName = skillView.findViewById<EditText>(R.id.skillName)
        val a = skillView.findViewById<CheckBox>(R.id.begineer)
        val b = skillView.findViewById<CheckBox>(R.id.intermediate)
        val c = skillView.findViewById<CheckBox>(R.id.advance)
        skillName.setText(skillname)
        if (strength=="Beginner"){
            a.isChecked
        }else if (strength=="Intermediate"){
            b.isChecked
        }
        else{
            c.isChecked
        }

        layoutcontainer.addView(skillView)

    }

    private fun savedata() {
        skillView   =LayoutInflater.from(this). inflate(R.layout.skill_details,layoutcontainer,false)
        var value : Boolean= false
        for (i in 0 until layoutcontainer.childCount){
            val SkillView = layoutcontainer.getChildAt(i)
            val SkillName = SkillView.findViewById<EditText>(R.id.skillName).text.toString()
            val a = SkillView.findViewById<CheckBox>(R.id.begineer)
            val b = SkillView.findViewById<CheckBox>(R.id.intermediate)
            val c = SkillView.findViewById<CheckBox>(R.id.advance)

            var grade : String
            if (a.isChecked){
                grade = "Beginner"
            }else if (b.isChecked){
                grade = "Intermediate"
            }
            else{
                grade = "Advance"
            }
            value = db.insertSkill(Resume_id,SkillName,grade)
        }
        if (value){
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Failed to filled Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun addskills(){
        skillView   =LayoutInflater.from(this). inflate(R.layout.skill_details,layoutcontainer,false)
        val delete : Button = skillView.findViewById(R.id.delete)
        delete.setOnClickListener{

            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout,null)
            dialog.setView(dialogView)

            val yes : Button = dialogView.findViewById(R.id.yes)
            val no : Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yes.setOnClickListener{
                layoutcontainer.removeView(skillView)
                if(layoutcontainer.childCount==0){
                    save.visibility=View.GONE
                }
                alertBox.dismiss()
            }

            no.setOnClickListener{
                alertBox.dismiss()
            }

            alertBox.show()

        }

        layoutcontainer.addView(skillView)
        save.visibility= View.VISIBLE
    }
}

