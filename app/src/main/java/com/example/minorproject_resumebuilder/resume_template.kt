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
    var resume_id : Int? = null
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
        val select_img2 : Button = findViewById(R.id.select_img2)
        val select_img1 : Button = findViewById(R.id.select_img1)
        val Resume_id = intent.getStringExtra("resume_id")
        resume_id = Resume_id?.toInt()

        val items = arrayOf("CLICK TO CHOOSE TEMPLATE DESIGN", "MEDICAL", "ENGINEERING", "IT FIELD","DIPLOMA","FRESHERS","MANAGEMENT","DESIGNING","BANKING")


        val adapter = ArrayAdapter(this,R.layout.simple_spinner_items,items)


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerItems.adapter = adapter

        spinnerItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                if (position == 1) {
                    img1.setImageResource(R.drawable.medical_1)
                    img2.setImageResource(R.drawable.medical_2)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                } else if (position == 2) {
                    img1.setImageResource(R.drawable.engineering_1)
                    img2.setImageResource(R.drawable.engineering_2)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }else if (position == 3) {
                    img1.setImageResource(R.drawable.it_1)
                    img2.setImageResource(R.drawable.it_2)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }else if (position == 4) {
                    img1.setImageResource(R.drawable.basic_3)
                    img2.setImageResource(R.drawable.design_1)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE

                }else if (position == 5) {
                    img1.setImageResource(R.drawable.basic_1)
                    img2.setImageResource(R.drawable.design_1)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }else if (position == 6) {
                    img1.setImageResource(R.drawable.basic_3)
                    img2.setImageResource(R.drawable.engineering_2)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }else if (position == 7) {
                    img1.setImageResource(R.drawable.design_1)
                    img2.setImageResource(R.drawable.it_1)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }else if (position == 8) {
                    img1.setImageResource(R.drawable.basic_1)
                    img2.setImageResource(R.drawable.engineering_1)
                    img1.visibility=View.VISIBLE
                    img2.visibility=View.VISIBLE
                    select_img1.visibility=View.VISIBLE
                    select_img2.visibility=View.VISIBLE
                }
                else{
                    img1.visibility=View.GONE
                    img2.visibility=View.GONE
                    select_img1.visibility=View.GONE
                    select_img2.visibility=View.GONE
                }



            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                img1.visibility=View.GONE
                img2.visibility=View.GONE
            }
        }

        select_img1.setOnClickListener{
            Toast.makeText(this,"Succesfully Data Filled",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Preview_template::class.java).apply {
                putExtra("value","medical_1")
                putExtra("resume_id",resume_id)
            }
            startActivity(intent)
        }


        select_img2.setOnClickListener{
            Toast.makeText(this,"Succesfully Created",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)
            finish()
        }



    }
}