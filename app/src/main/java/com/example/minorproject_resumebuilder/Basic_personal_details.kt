package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class Basic_personal_details : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private lateinit var photo: ImageView
    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private lateinit var save   :   Button
    private lateinit var dob    :   EditText
    private lateinit var email    :   EditText
    private lateinit var fname    :   EditText
    private lateinit var lname    :   EditText
    private lateinit var phone    :   EditText
    private lateinit var nationality    :   EditText
    private lateinit var gender:String
    private  var Resume_id  :   Long = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_basic_personal_details)

        photo = findViewById(R.id.profilePhoto)
        share = SharePrefrence(this)
        save = findViewById(R.id.save)
        dob = findViewById(R.id.dob)
        email  = findViewById(R.id.emailId)
        fname   = findViewById(R.id.fname)
        lname = findViewById(R.id.lname)
        val male: CheckBox = findViewById(R.id.male)
        val female: CheckBox = findViewById(R.id.female)
        nationality = findViewById(R.id.nationality)
       phone = findViewById(R.id.phone)
       Resume_id = share.getResumeId()
        share = SharePrefrence(this)
        db = SQLiteHelper(this)

        val personalDetail = db.getPersonalDetails(Resume_id)
        if (personalDetail != null) {
            personalDetail?.let {
                email.setText(it.email)
                fname.setText(it.fname)
                lname.setText(it.lname)
                dob.setText(it.dateOfBirth)
                phone.setText(it.phone)
                if (it.gender == "Male") {
                    male.isChecked
                } else {
                    if (it.gender == "Female") {
                        female.isChecked
                    } else {
                        Toast.makeText(this, "Cannot Find gender", Toast.LENGTH_SHORT).show()
                    }
                }
                nationality.setText(it.nationality)
            }
            updateDetails()
        }
        save.setOnClickListener {
            val Email = email.text.toString().trim()
            val Phone = phone.text.toString().trim()
            val Fname = fname.text.toString().trim()
            val Lname = lname.text.toString().trim()
            val Nationality = nationality.text.toString().trim()
            val dob = dob.text.toString().trim()


            if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
                Toast.makeText(this, "Please Fill Require details", Toast.LENGTH_SHORT).show()
            } else if (!male.isChecked && !female.isChecked) {
                Toast.makeText(this, "Please Fill Require details", Toast.LENGTH_SHORT).show()
            } else if (male.isChecked && female.isChecked) {
                Toast.makeText(this, "Please Select One gender only", Toast.LENGTH_SHORT).show()
            } else {
                if (male.isChecked) {
                    gender = "Male"
                } else {
                    gender = "Female"
                }
                val value = db.insertPersonalDetails(
                    Resume_id,
                    Fname,
                    Lname,
                    Phone,
                    Email,
                    Nationality,
                    gender!!,
                    dob,
                    "profileemage"
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

        dob.setOnClickListener {
            val datepicker = DatePickerDialog(
                this, { _, year, month, dayOfmonth ->
                    val selectDate = Calendar.getInstance()
                    selectDate.set(year, month, dayOfmonth)
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    dob.setText(dateFormat.format(selectDate.time))
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }
        photo.setOnClickListener {

            imagePickerLauncher.launch("image/*")
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                photo.setImageURI(uri)
            }
        }

    private fun updateDetails(){
        save.setOnClickListener{
            val Email = email.text.toString().trim()
            val Phone = phone.text.toString().trim()
            val Fname = fname.text.toString().trim()
            val Lname = lname.text.toString().trim()
            val Nationality = nationality.text.toString().trim()
            val dob = dob.text.toString().trim()

            val male: CheckBox = findViewById(R.id.male)
            val female: CheckBox = findViewById(R.id.female)


            if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
                Toast.makeText(this, "Please Fill Require details", Toast.LENGTH_SHORT).show()
            } else if (!male.isChecked && !female.isChecked) {
                Toast.makeText(this, "Please Fill Require details", Toast.LENGTH_SHORT).show()
            } else if (male.isChecked && female.isChecked) {
                Toast.makeText(this, "Please Select One gender only", Toast.LENGTH_SHORT).show()
            } else {
                if (male.isChecked) {
                    gender = "Male"
                } else {
                    gender = "Female"
                }
                val value = db.updatePersonalDetails(Resume_id,
                    Fname,
                    Lname,
                    Phone,
                    Email,
                    Nationality,
                    gender,
                    dob,
                    "hello")

                if (value) {
                    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
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
    }

}
