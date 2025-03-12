package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SkillDetailsActivity : AppCompatActivity() {

    private lateinit var addSkillButton: Button
    private lateinit var saveButton: Button
    private lateinit var layoutContainer: LinearLayout
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill_details)

        addSkillButton = findViewById(R.id.addSkill)
        layoutContainer = findViewById(R.id.layoutContainer)
        saveButton = findViewById(R.id.savebtn)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid

        if (userId != null) {
            loadSkills()
        }

        updateSaveButtonVisibility()

        addSkillButton.setOnClickListener {
            addSkillView()
        }

        saveButton.setOnClickListener {
            saveSkillsToFirebase()
        }
    }

    private fun addSkillView(skillName: String = "", strength: String = "", skillId: String? = null) {
        val skillView = LayoutInflater.from(this).inflate(R.layout.skill_details, layoutContainer, false)
        val skillEditText = skillView.findViewById<EditText>(R.id.skillName)
        val beginnerCheckBox = skillView.findViewById<CheckBox>(R.id.begineer)
        val intermediateCheckBox = skillView.findViewById<CheckBox>(R.id.intermediate)
        val advancedCheckBox = skillView.findViewById<CheckBox>(R.id.advance)
        val deleteButton = skillView.findViewById<Button>(R.id.delete)

        skillEditText.setText(skillName)
        when (strength) {
            "Beginner" -> beginnerCheckBox.isChecked = true
            "Intermediate" -> intermediateCheckBox.isChecked = true
            "Advanced" -> advancedCheckBox.isChecked = true
        }

        skillView.tag = skillId // Store Firestore document ID

        deleteButton.setOnClickListener {
            showDeleteDialog(skillView, skillId)
        }

        layoutContainer.addView(skillView)
        updateSaveButtonVisibility()
    }

    private fun saveSkillsToFirebase() {
        if (userId == null) return

        val skillsCollection = firestore.collection("users").document(userId!!).collection("skills")
        var isSuccess = true

        for (i in 0 until layoutContainer.childCount) {
            val skillView = layoutContainer.getChildAt(i)
            val skillName = skillView.findViewById<EditText>(R.id.skillName).text.toString()
            val beginnerCheckBox = skillView.findViewById<CheckBox>(R.id.begineer)
            val intermediateCheckBox = skillView.findViewById<CheckBox>(R.id.intermediate)
            val advancedCheckBox = skillView.findViewById<CheckBox>(R.id.advance)

            val skillId = skillView.tag as? String ?: skillsCollection.document().id
            val skillStrength = when {
                beginnerCheckBox.isChecked -> "Beginner"
                intermediateCheckBox.isChecked -> "Intermediate"
                else -> "Advanced"
            }

            val skillData = mapOf(
                "skillName" to skillName,
                "strength" to skillStrength
            )

            skillsCollection.document(skillId)
                .set(skillData)
                .addOnFailureListener {
                    isSuccess = false
                }
        }

        if (isSuccess) {
            Toast.makeText(this, "Skills saved successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java))
            finish()
        } else {
            Toast.makeText(this, "Failed to save skills", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSkills() {
        if (userId == null) return

        firestore.collection("users").document(userId!!)
            .collection("skills")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val skillName = document.getString("skillName") ?: ""
                    val strength = document.getString("strength") ?: ""
                    addSkillView(skillName, strength, document.id)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load skills", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDeleteDialog(skillView: View, skillId: String?) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null)
        dialog.setView(dialogView)

        val yesButton: Button = dialogView.findViewById(R.id.yes)
        val noButton: Button = dialogView.findViewById(R.id.no)

        val alertBox = dialog.create()

        yesButton.setOnClickListener {
            if (skillId != null && userId != null) {
                firestore.collection("users").document(userId!!)
                    .collection("skills").document(skillId)
                    .delete()
                    .addOnSuccessListener {
                        layoutContainer.removeView(skillView)
                        updateSaveButtonVisibility()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete skill", Toast.LENGTH_SHORT).show()
                    }
            } else {
                layoutContainer.removeView(skillView)
                updateSaveButtonVisibility()
            }
            alertBox.dismiss()
        }

        noButton.setOnClickListener {
            alertBox.dismiss()
        }

        alertBox.show()
    }

    private fun updateSaveButtonVisibility() {
        saveButton.visibility = if (layoutContainer.childCount > 0) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Create_resume::class.java))
    }
}
