package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
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
import java.text.SimpleDateFormat
import java.util.*


class Basic_personal_details : AppCompatActivity() {
    private val calendar = Calendar.getInstance()
    private lateinit var photo: ImageView
    private lateinit var db : SQLiteHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_basic_personal_details)

        photo = findViewById(R.id.profilePhoto)
        val save :Button = findViewById(R.id.save)
        val dob :EditText=findViewById(R.id.dob)
        val email :EditText=findViewById(R.id.emailId)
        val fname :EditText=findViewById(R.id.fname)
        val lname :EditText=findViewById(R.id.lname)
        val male :CheckBox=findViewById(R.id.male)
        val female :CheckBox=findViewById(R.id.female)
        val nationality :EditText=findViewById(R.id.nationality)
        val phone :EditText=findViewById(R.id.phone)

        db = SQLiteHelper(this)
        save.setOnClickListener{


            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Create_resume::class.java)
            startActivity(intent)
        }

        dob.setOnClickListener{
            val datepicker = DatePickerDialog(this,{ _,year,month,dayOfmonth ->
                val selectDate = Calendar.getInstance()
                selectDate.set(year,month,dayOfmonth)
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

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            photo.setImageURI(uri)
        }
    }
}
