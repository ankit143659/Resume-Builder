package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class LoginPage : AppCompatActivity() {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var prefrence: SharePrefrence


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
        prefrence= SharePrefrence(this)

        login.setOnClickListener{
            val username=Username.text.toString()
            val password = Password.text.toString()

            val userDetails = dbHelper.checkUser(username,password)
             if(userDetails!=null){
                 prefrence.saveUserDetails(userDetails)
                 Toast.makeText(this, "login SuccesFully", Toast.LENGTH_SHORT).show()
                 prefrence.setLoggedIn(true)
                 val intent = Intent(this, ViewPager::class.java)
                 startActivity(intent)
             }
            else{
                usernotexit.text="Invalid username or password"
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

    @SuppressLint("MissingInflatedId")
    fun showdialogBox (){
        val builder = AlertDialog.Builder(this)
        val builderView = LayoutInflater.from(this) .inflate(R.layout.forget_email,null)
        builder.setView(builderView)

        val recover : Button= builderView.findViewById(R.id.recover)
        val cancle : Button= builderView.findViewById(R.id.cancle)
        val email : EditText= builderView.findViewById(R.id.email)

        val alert = builder.create()
        recover.setOnClickListener(){
            if(email.text.toString().isNotEmpty()){
                val password = dbHelper.getpasswordbyemail(email.text.toString())
                if (password!=null){
                    showpasswordDialog(password)
                    alert.dismiss()
                    
                }
                else{
                    Toast.makeText(this,"Email not found",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please enter a email",Toast.LENGTH_SHORT).show()
            }
        }
        cancle.setOnClickListener{alert.dismiss()}
        alert.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showpasswordDialog(password:String) {
        val builder = AlertDialog.Builder(this)
        val builderView = LayoutInflater.from(this).inflate(R.layout.forgot_password,null)
        builder.setView(builderView)
        val Password :TextView = builderView.findViewById(R.id.pass)
        val ok :Button = builderView.findViewById(R.id.ok)
        val alert = builder.create()
        Password.text=password
        ok.setOnClickListener{alert.dismiss()}
        alert.show()
    }
}