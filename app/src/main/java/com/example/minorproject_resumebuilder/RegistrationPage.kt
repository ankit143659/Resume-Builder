package com.example.minorproject_resumebuilder


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Patterns
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper

class
RegistrationPage : AppCompatActivity() {
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


            if(Username.length()<3 && Username.length()>15 || emailId.length()==0 || Password.length()==0){
                Username.setError("Username required")
                emailId.setError("EmailId required")
                Password.setError("Password required")
            }else if(phone.length()!=10){
                phone.setError("10 digit phone number allowed")
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailId.setError("Invalid Email Format")
            }else if (!password.matches(Regex(".*[A-Z].*"))){
                Password.setError("Passwor must contain a Capital Letter,small letter.digits and atleast a single special Charachter")
            }else if (!password.matches(Regex(".*[a-z].*"))){
                Password.setError("Passwor must contain a Capital Letter,small letter.digits and atleast a single special Charachter")
            }else if (!password.matches(Regex(".*[0-9].*"))){
                Password.setError("Passwor must contain a Capital Letter,small letter.digits and atleast a single special Charachter")
            }else if (!password.matches(Regex(".*[!@#$%^&*()_+=|<>{}\\[\\]~-].*"))){
                Password.setError("Passwor must contain a Capital Letter,small letter.digits and atleast a single special Charachter")
            }/*else if (!username.matches(Regex(".^[a-zA-z0-9]+$"))){
                Username.setError("Username can contain only letters and alphabets")
            }*/

            else{
                if (addUser(username, password, Phone, email)) {
                    Toast.makeText(this, "SuccessFully Register", Toast.LENGTH_SHORT).show()
                    val I= Intent(this,LoginPage::class.java)
                    startActivity(I)
                    finish()
                } else {
                    Toast.makeText(this, "User already exists or error!", Toast.LENGTH_SHORT).show()
                }


            }

        }
        regis.setOnClickListener{
            val I= Intent(this,LoginPage::class.java)
            startActivity(I)
        }

    }

    private fun addUser(username: String, password: String, phone: String, email: String): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(SQLiteHelper.COLUMN_USERNAME, username)
                put(SQLiteHelper.COLUMN_PASSWORD, password)
                put(SQLiteHelper.COLUMN_PHONE, phone)
                put(SQLiteHelper.COLUMN_EMAIL, email)
            }
            val result = db.insert(SQLiteHelper.TABLE_USERS, null, values)
            result != -1L
        } catch (e: Exception) {
            false
        }
    }


}