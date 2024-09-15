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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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
    private lateinit var nationality: Spinner
    private lateinit var gender: String
    private lateinit var adapter: ArrayAdapter<String>
    private var Resume_id: Long = 0
    private var imageData: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val READ_EXTERNAL_STORAGE_REQUEST = 100
    }

    @SuppressLint("MissingInflatedId", "ResourceType")
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

        // Check and request necessary permissions
        checkAndRequestPermissions()

        // Set up spinner
        val items = resources.getStringArray(R.array.nationality_items)
        adapter = ArrayAdapter(this, R.layout.simple_spinner_items, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nationality.adapter = adapter

        // Load existing personal details if available
        val personalDetail = db.getPersonalDetails(Resume_id)
        if (personalDetail != null) {
            populateFieldsWithExistingData(personalDetail)
        } else {
            save.setOnClickListener { saveDetails(male, female) }
        }

        // Date picker for DOB
        dob.setOnClickListener { showDatePicker() }

        // Open gallery to pick image
        photo.setOnClickListener { openGallery() }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    this,
                    "Storage permission is needed to load images.",
                    Toast.LENGTH_LONG
                ).show()
            }

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST
            )
        } else {
            openGallery()
        }
    }

    private fun showDatePicker() {
        val datepicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectDate = Calendar.getInstance()
                selectDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                dob.setText(dateFormat.format(selectDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datepicker.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data?.data != null) {
            imageData = data.data
            Log.d("ImageUri", "Selected Image URI: $imageData")
            loadAndDisplayImage(imageData)
        }
    }

    private fun loadAndDisplayImage(imageUri: Uri?) {
        if (imageUri == null) return
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            photo.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyImageToInternalStorage(imageUri: Uri): String? {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun saveDetails(male: CheckBox, female: CheckBox) {
        val Email = email.text.toString().trim()
        val Phone = phone.text.toString().trim()
        val Fname = fname.text.toString().trim()
        val Lname = lname.text.toString().trim()
        val Nationality = nationality.selectedItem.toString().trim() // Get spinner value
        val dob = dob.text.toString().trim()

        if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
            Toast.makeText(this, "Please fill all required details", Toast.LENGTH_SHORT).show()
            return
        }

        gender = if (male.isChecked) "Male" else "Female"

        // Copy the image to internal storage
        val profileImagePath = imageData?.let { copyImageToInternalStorage(it) } ?: ""

        val value = db.insertPersonalDetails(
            Resume_id,
            Fname,
            Lname,
            Phone,
            Email,
            Nationality,
            gender,
            dob,
            profileImagePath
        )

        if (value) {
            Toast.makeText(this, "Successfully filled data", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", Resume_id)
            })
            finish()
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateFieldsWithExistingData(personalDetail: PersonalDetail) {
        email.setText(personalDetail.email)
        fname.setText(personalDetail.fname)
        lname.setText(personalDetail.lname)
        dob.setText(personalDetail.dateOfBirth)
        phone.setText(personalDetail.phone)

        // Set the nationality spinner to the correct value
        val nationalityList = resources.getStringArray(R.array.nationality_items)
        val nationalityIndex = nationalityList.indexOf(personalDetail.nationality)
        if (nationalityIndex >= 0) {
            nationality.setSelection(nationalityIndex)
        }

        // Set profile image, gender, and other fields as before
        if (personalDetail.profileImage != null) {
            val imageFile = File(personalDetail.profileImage)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                photo.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
            }
        }

        if (personalDetail.gender == "Male") {
            findViewById<CheckBox>(R.id.male).isChecked = true
        } else if (personalDetail.gender == "Female") {
            findViewById<CheckBox>(R.id.female).isChecked = true
        }

        save.setOnClickListener { personalDetail.profileImage?.let { it1 -> updateDetails(it1) } }
    }

    private fun updateDetails(existingImagePath: String) {
        val Email = email.text.toString().trim()
        val Phone = phone.text.toString().trim()
        val Fname = fname.text.toString().trim()
        val Lname = lname.text.toString().trim()
        val Nationality = nationality.selectedItem.toString().trim() // Get spinner value
        val dob = dob.text.toString().trim()

        if (Email.isEmpty() || Phone.isEmpty() || Fname.isEmpty() || Lname.isEmpty() || Nationality.isEmpty()) {
            Toast.makeText(this, "Please fill all required details", Toast.LENGTH_SHORT).show()
            return
        }

        gender = if (findViewById<CheckBox>(R.id.male).isChecked) "Male" else "Female"

        // Check if a new image is selected, otherwise use existing one
        val profileImagePath = imageData?.let { copyImageToInternalStorage(it) } ?: existingImagePath

        val value = db.updatePersonalDetails(
            Resume_id,
            Fname,
            Lname,
            Phone,
            Email,
            Nationality,
            gender,
            dob,
            profileImagePath
        )

        if (value) {
            Toast.makeText(this, "Successfully updated data", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Create_resume::class.java).apply {
                putExtra("resume_id", Resume_id)
            })
            finish()
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
        }
    }
}
