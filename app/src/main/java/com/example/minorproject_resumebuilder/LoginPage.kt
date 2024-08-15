package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val forgetPass :TextView = findViewById(R.id.textViewForgotPassword)

        dbHelper=SQLiteHelper(this)

        login.setOnClickListener{
            val username=Username.text.toString()
            val password = Password.text.toString()


             if(dbHelper.checkUser(username,password)){
                 Toast.makeText(this, "login SuccesFully", Toast.LENGTH_SHORT).show()
                 val intent = Intent(this, ViewPager::class.java)
                 startActivity(intent)
             }
            else{
                 Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
             }
        }
        regis.setOnClickListener{
            val I= Intent(this,RegistrationPage::class.java)
            startActivity(I)
        }

        forgetPass.setOnClickListener{
            showdialogBox()
        }
    }

    fun showdialogBox (){
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = " Enter Your email"
        builder.setView(input)
        builder.setPositiveButton("Reset Password"){dailog,which ->
             val email = input.text.toString()
            if(email.isNotEmpty()){
                val password = dbHelper.getpasswordbyemail(email)
                if (password!=null){
                    showpasswordDialog(password)
                }
                else{
                    Toast.makeText(this,"Email not found",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please enter a email",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("cancle"){dialog,_which-> dialog.dismiss()}
        builder.show()
    }

    private fun showpasswordDialog(password:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Your Password")

        val passwordTextview = TextView(this)
        passwordTextview.text=password
        builder.setView(passwordTextview)
        builder.setPositiveButton("ok"){dailog, which-> dailog.dismiss()}

        builder.show()
    }
}