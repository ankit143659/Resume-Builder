package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val login: Button = findViewById(R.id.buttonLogin)
        val regis: TextView = findViewById(R.id.textViewSignUp)
        val username: EditText = findViewById(R.id.Username)
        val password: EditText = findViewById(R.id.Password)
        val userNotExist: TextView = findViewById(R.id.usernotexit)
        val forgetPass: TextView = findViewById(R.id.textViewForgotPassword)

        auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val email = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ViewPager::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            userNotExist.text = "Invalid username or password"
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        regis.setOnClickListener {
            val intent = Intent(this, RegistrationPage::class.java)
            startActivity(intent)
        }

        forgetPass.setOnClickListener {
            showDialogBox()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showDialogBox() {
        val builder = AlertDialog.Builder(this)
        val builderView = LayoutInflater.from(this).inflate(R.layout.forget_email, null)
        builder.setView(builderView)

        val recover: Button = builderView.findViewById(R.id.recover)
        val cancel: Button = builderView.findViewById(R.id.cancle)
        val email: EditText = builderView.findViewById(R.id.email)

        val alert = builder.create()

        recover.setOnClickListener {
            val emailText = email.text.toString().trim()
            if (emailText.isNotEmpty()) {
                auth.sendPasswordResetEmail(emailText)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                        alert.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        cancel.setOnClickListener { alert.dismiss() }
        alert.show()
    }
}
