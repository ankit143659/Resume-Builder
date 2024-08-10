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
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.User

class RegistrationPage : AppCompatActivity() {
    private lateinit var dbHelper: SQLiteHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        val login: Button =findViewById(R.id.buttonRegister);
        val regis: TextView =findViewById(R.id.textViewLogin);
        dbHelper=SQLiteHelper(this)

        val Username : EditText = findViewById(R.id.editTextUsername)
        val emailId : EditText = findViewById(R.id.editTextEmail)
        val Password : EditText = findViewById(R.id.editTextPassword)
        val phone : EditText = findViewById(R.id.ph)



        login.setOnClickListener{

            val username = Username.text.toString()
            val password = Password.text.toString()
            val email = emailId.text.toString()
            val Phone = phone.text.toString()


            if(Username.length()==0 || emailId.length()==0 || Password.length()==0){
                Username.setError("Username required")
                emailId.setError("EmailId required")
                Password.setError("Password required")

            }

            else if(phone.length()!=10){
                phone.setError("10 digit phone number allowed")

            }
            else{
                val user = User(username = username, password = password, email = email, phone = Phone)
                val result = dbHelper.addUser(user)
                if (result > 0) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }


            }

        }
        regis.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }

    }


}