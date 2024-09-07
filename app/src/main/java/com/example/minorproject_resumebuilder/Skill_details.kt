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

class Skill_details : AppCompatActivity() {

    private lateinit var addLayout: Button
    private lateinit var save: Button
    private lateinit var layoutContainer: LinearLayout
    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private var resumeId: Long? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skill_details)

        share = SharePrefrence(this)
        addLayout = findViewById(R.id.addSkill)
        layoutContainer = findViewById(R.id.layoutContainer)
        save = findViewById(R.id.savebtn)
        db = SQLiteHelper(this)
        resumeId = share.getResumeId()

        // Load existing skill details if available
        val skillDetails = db.getAllSkills(resumeId)
        if (skillDetails != null && skillDetails.isNotEmpty()) {
            loadDetails(skillDetails)
        }

        addLayout.setOnClickListener {
            addSkills()
        }

        save.setOnClickListener {
            saveData()
        }
    }

    private fun loadDetails(skillDetails: List<Skill>) {
        skillDetails.forEach { skill ->
            loadSkillDetails(skill.skillName, skill.strength)
        }
    }

    private fun loadSkillDetails(skillName: String, strength: String) {
        val skillView = LayoutInflater.from(this).inflate(R.layout.skill_details, layoutContainer, false)

        val skillNameField = skillView.findViewById<EditText>(R.id.skillName)
        val beginnerCheckbox = skillView.findViewById<CheckBox>(R.id.begineer)
        val intermediateCheckbox = skillView.findViewById<CheckBox>(R.id.intermediate)
        val advancedCheckbox = skillView.findViewById<CheckBox>(R.id.advance)

        skillNameField.setText(skillName)

        when (strength) {
            "Beginner" -> beginnerCheckbox.isChecked = true
            "Intermediate" -> intermediateCheckbox.isChecked = true
            "Advance" -> advancedCheckbox.isChecked = true
        }

        layoutContainer.addView(skillView)
    }

    private fun updateDetails(skillId: Long) {
        var updateSuccessful = true

        for (i in 0 until layoutContainer.childCount) {
            val skillView = layoutContainer.getChildAt(i)
            val skillName = skillView.findViewById<EditText>(R.id.skillName).text.toString()
            val strength = when {
                skillView.findViewById<CheckBox>(R.id.begineer).isChecked -> "Beginner"
                skillView.findViewById<CheckBox>(R.id.intermediate).isChecked -> "Intermediate"
                else -> "Advance"
            }

            val updated = db.updateSkill(skillId, skillName, strength)
            if (!updated) {
                updateSuccessful = false
            }
        }

        if (updateSuccessful) {
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java))
            finish()
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        var saveSuccessful = true

        for (i in 0 until layoutContainer.childCount) {
            val skillView = layoutContainer.getChildAt(i)
            val skillName = skillView.findViewById<EditText>(R.id.skillName).text.toString()
            val strength = when {
                skillView.findViewById<CheckBox>(R.id.begineer).isChecked -> "Beginner"
                skillView.findViewById<CheckBox>(R.id.intermediate).isChecked -> "Intermediate"
                else -> "Advance"
            }

            val inserted = db.insertSkill(resumeId, skillName, strength)
            if (!inserted) {
                saveSuccessful = false
            }
        }

        if (saveSuccessful) {
            Toast.makeText(this, "Successfully filled data", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java))
            finish()
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addSkills() {
        val skillView = LayoutInflater.from(this).inflate(R.layout.skill_details, layoutContainer, false)

        val deleteButton: Button = skillView.findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
            dialog.setView(dialogView)

            val yesButton: Button = dialogView.findViewById(R.id.yes)
            val noButton: Button = dialogView.findViewById(R.id.no)

            val alertBox = dialog.create()

            yesButton.setOnClickListener {
                layoutContainer.removeView(skillView)
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

        layoutContainer.addView(skillView)
        save.visibility = View.VISIBLE
    }
}
