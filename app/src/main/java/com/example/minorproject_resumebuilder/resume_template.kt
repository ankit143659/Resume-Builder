package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class resume_template : AppCompatActivity() {

    private lateinit var spinnerItems: Spinner
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resume_template)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerItems = findViewById(R.id.spinner_items)
        val img1 : ImageView = findViewById(R.id.img1)
        val img2 : ImageView = findViewById(R.id.img2)
        val img3 : ImageView = findViewById(R.id.img3)
        val img4 : ImageView = findViewById(R.id.img4)

        val items = arrayOf("Select an item", "MEDICAL", "ENGINEERING", "IT FIELD","UNDER GRADUATE","GRADUATE")


        val adapter = ArrayAdapter(this,R.layout.simple_spinner_items)


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerItems.adapter = adapter

        spinnerItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                if (position == 1) {
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    img4.visibility=View.GONE
                    img3.visibility=View.GONE
                } else if (position == 2) {
                    img1.visibility=View.GONE
                    img2.visibility=View.GONE
                    img4.visibility=View.VISIBLE
                    img3.visibility=View.VISIBLE
                }
                else{
                    img1.visibility=View.GONE
                    img2.visibility=View.GONE
                    img4.visibility=View.GONE
                    img3.visibility=View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                img1.visibility=View.GONE
                img2.visibility=View.GONE
                img4.visibility=View.GONE
                img3.visibility=View.GONE
            }
        }

        val save : Button = findViewById(R.id.save)
        save.setOnClickListener{
            Toast.makeText(this,"Successfully filled Data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }
}