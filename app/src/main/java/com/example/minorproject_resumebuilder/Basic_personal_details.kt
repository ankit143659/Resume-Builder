package com.example.minorproject_resumebuilder

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class Basic_personal_details : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var photo: ImageView
    private lateinit var save: Button
    private lateinit var dob: EditText
    private lateinit var email: EditText
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var phone: EditText
    private lateinit var nationality: Spinner
    private lateinit var gender: String
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var share: SharePrefrence
    private var Resume_id: String = ""
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
        Resume_id = share.getResumeId().toString() // Convert ID to String for Firebase
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Check and request permissions
        checkAndRequestPermissions()

        // Set up spinner
        val items = resources.getStringArray(R.array.nationality_items)
        adapter = ArrayAdapter(this, R.layout.simple_spinner_items, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nationality.adapter = adapter

        // Load existing details from Firestore
        loadPersonalDetails()

        // Date picker for DOB
        dob.setOnClickListener { showDatePicker() }

        // Open gallery to pick image
        photo.setOnClickListener { openGallery() }

        save.setOnClickListener { saveDetails(male, female) }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST
            )
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                dob.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
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
            photo.setImageURI(imageData)
        }
    }

    private fun saveDetails(male: CheckBox, female: CheckBox) {
        val emailText = email.text.toString().trim()
        val phoneText = phone.text.toString().trim()
        val fnameText = fname.text.toString().trim()
        val lnameText = lname.text.toString().trim()
        val nationalityText = nationality.selectedItem.toString().trim()
        val dobText = dob.text.toString().trim()

        if (emailText.isEmpty() || phoneText.isEmpty() || fnameText.isEmpty() || lnameText.isEmpty() || nationalityText.isEmpty()) {
            Toast.makeText(this, "Please fill all required details", Toast.LENGTH_SHORT).show()
            return
        }

        gender = if (male.isChecked) "Male" else "Female"

        if (imageData != null) {
            uploadImageToFirebase(imageData!!) { imageUrl ->
                saveDataToFirestore(fnameText, lnameText, phoneText, emailText, nationalityText, gender, dobText, imageUrl)
            }
        } else {
            saveDataToFirestore(fnameText, lnameText, phoneText, emailText, nationalityText, gender, dobText, null)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, callback: (String) -> Unit) {
        val storageRef = storage.reference.child("profile_images/${Resume_id}.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString()) // Pass the image URL back
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDataToFirestore(fname: String, lname: String, phone: String, email: String, nationality: String, gender: String, dob: String, imageUrl: String?) {
        val userMap = hashMapOf(
            "firstName" to fname,
            "lastName" to lname,
            "phone" to phone,
            "email" to email,
            "nationality" to nationality,
            "gender" to gender,
            "dob" to dob,
            "profileImageUrl" to (imageUrl ?: "")
        )

        firestore.collection("users").document(Resume_id)
            .set(userMap)
            .addOnSuccessListener {
                share.storePersonalDetails(true)
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Create_resume::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadPersonalDetails() {
        firestore.collection("users").document(Resume_id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    email.setText(document.getString("email"))
                    fname.setText(document.getString("firstName"))
                    lname.setText(document.getString("lastName"))
                    phone.setText(document.getString("phone"))
                    dob.setText(document.getString("dob"))
                    val genderValue = document.getString("gender")
                    if (genderValue == "Male") findViewById<CheckBox>(R.id.male).isChecked = true
                    else findViewById<CheckBox>(R.id.female).isChecked = true
                    val imageUrl = document.getString("profileImageUrl")
                    if (!imageUrl.isNullOrEmpty()) Glide.with(this).load(imageUrl).into(photo)
                }
            }
    }
}
