package com.example.minorproject_resumebuilder

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*

class Basic_personal_details : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private lateinit var photo: ImageView
    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private lateinit var save: Button
    private lateinit var dob: EditText
    private lateinit var email: EditText
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var phone: EditText
    private lateinit var nationality: EditText
    private lateinit var gender: String
    private var Resume_id: Long = 0
    private var imageData: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val READ_EXTERNAL_STORAGE_REQUEST = 100
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_personal_details)

        photo = findViewById(R.id.profilePhoto)
        share = SharePrefrence(this)
        save = findViewById(R.id.save)
        dob = findViewById(R.id.dob)
        email = findViewById(R.id.emailId)
        fname = findViewById(R.id.fname)
        lname = findViewById(R.id.lname)
        val male: CheckBox = findViewById(R.id.male)
        val female: CheckBox = findViewById(R.id.female)
        nationality = findViewById(R.id.nationality)
        phone = findViewById(R.id.phone)
        Resume_id = share.getResumeId()
        db = SQLiteHelper(this)

        checkAndRequestPermissions()

        val personalDetail = db.getPersonalDetails(Resume_id)
        if (personalDetail != null) {
            email.setText(personalDetail.email)
            fname.setText(personalDetail.fname)
            lname.setText(personalDetail.lname)
            dob.setText(personalDetail.dateOfBirth)
            phone.setText(personalDetail.phone)
            if (personalDetail.gender == "Male") {
                male.isChecked = true
            } else if (personalDetail.gender == "Female") {
                female.isChecked = true
            } else {
                Toast.makeText(this, "Cannot Find gender", Toast.LENGTH_SHORT).show()
            }
            nationality.setText(personalDetail.nationality)
            imageData = Uri.parse(personalDetail.profileImage)
            photo.setImageURI(imageData)

            updateDetails()
        } else {
            save.setOnClickListener {
                saveDetails(male, female)
            }
        }

        dob.setOnClickListener {
            val datepicker = DatePickerDialog(
                this, { _, year, month, dayOfMonth ->
                    val selectDate = Calendar.getInstance()
                    selectDate.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    dob.setText(dateFormat.format(selectDate.time))
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }

        photo.setOnClickListener {
            openGallery()
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageData = data.data
            if (imageData != null) {
                // Logging the URI for debugging
                Log.d("ImageUri", "Selected Image URI: $imageData")
                try {
                    Glide.with(this)
                        .load(imageData)
                        .into(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDetails(male: CheckBox, female: CheckBox) {
        val Email = email.text.toString().trim()
        val Phone = phone.text.toString().trim()
        val Fname = fname.text.toString().trim()
        val Lname = lname.text.toString().trim()
        val Nationality = nationality.text.toString().trim()
        val dob = dob.text.toString().trim()

        if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
            Toast.makeText(this, "Please Fill Required details", Toast.LENGTH_SHORT).show()
        } else if (!male.isChecked && !female.isChecked) {
            Toast.makeText(this, "Please Select a gender", Toast.LENGTH_SHORT).show()
        } else if (male.isChecked && female.isChecked) {
            Toast.makeText(this, "Please Select One gender only", Toast.LENGTH_SHORT).show()
        } else {
            gender = if (male.isChecked) {
                "Male"
            } else {
                "Female"
            }

            val value = db.insertPersonalDetails(
                Resume_id,
                Fname,
                Lname,
                Phone,
                Email,
                Nationality,
                gender,
                dob,
                imageData?.toString() ?: "" // Handle null case
            )

            if (value) {
                Toast.makeText(this, "Successfully filled Data", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Create_resume::class.java).apply {
                    putExtra("resume_id", Resume_id)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDetails() {
        save.setOnClickListener {
            val Email = email.text.toString().trim()
            val Phone = phone.text.toString().trim()
            val Fname = fname.text.toString().trim()
            val Lname = lname.text.toString().trim()
            val Nationality = nationality.text.toString().trim()
            val dob = dob.text.toString().trim()

            val male: CheckBox = findViewById(R.id.male)
            val female: CheckBox = findViewById(R.id.female)

            if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
                Toast.makeText(this, "Please Fill Required details", Toast.LENGTH_SHORT).show()
            } else if (!male.isChecked && !female.isChecked) {
                Toast.makeText(this, "Please Select a gender", Toast.LENGTH_SHORT).show()
            } else if (male.isChecked && female.isChecked) {
                Toast.makeText(this, "Please Select One gender only", Toast.LENGTH_SHORT).show()
            } else {
                gender = if (male.isChecked) {
                    "Male"
                } else {
                    "Female"
                }

                val value = db.updatePersonalDetails(
                    Resume_id,
                    Fname,
                    Lname,
                    Phone,
                    Email,
                    Nationality,
                    gender,
                    dob,
                    imageData?.toString() ?: "" // Handle null case
                )

                if (value) {
                    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Create_resume::class.java).apply {
                        putExtra("resume_id", Resume_id)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
