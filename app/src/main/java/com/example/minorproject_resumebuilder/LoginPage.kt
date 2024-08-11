package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper

class LoginPage : AppCompatActivity() {
    private lateinit var dbHelper: SQLiteHelper
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val login: Button =findViewById(R.id.buttonLogin);
        val regis: TextView =findViewById(R.id.textViewSignUp);
        val Username: EditText=findViewById(R.id.Username)
        val Password : EditText = findViewById(R.id.Password)
        val usernotexit :TextView = findViewById(R.id.usernotexit)

        dbHelper=SQLiteHelper(this)

        login.setOnClickListener{
            val username=Username.text.toString()
            val password = Password.text.toString()

            val userExists = dbHelper.checkUser(username,password)

            if (userExists){
                Toast.makeText(this,"login SuccesFully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            }
        }
        regis.setOnClickListener{
            val I= Intent(this,RegistrationPage::class.java)
            startActivity(I)
        }
    }
}