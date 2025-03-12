package com.example.minorproject_resumebuilder

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)

        val register: Button = findViewById(R.id.buttonRegister)
        val login: TextView = findViewById(R.id.textViewLogin)

        val username: EditText = findViewById(R.id.editTextUsername)
        val emailId: EditText = findViewById(R.id.editTextEmail)
        val password: EditText = findViewById(R.id.editTextPassword)
        val phone: EditText = findViewById(R.id.ph)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        register.setOnClickListener {
            val usernameText = username.text.toString().trim()
            val emailText = emailId.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val phoneText = phone.text.toString().trim()

            if (validateInputs(usernameText, emailText, passwordText, phoneText)) {
                registerUser(usernameText, emailText, passwordText, phoneText)
            }
        }

        login.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }
    }

    private fun validateInputs(username: String, email: String, password: String, phone: String): Boolean {
        when {
            username.length !in 3..15 -> {
                showError(findViewById(R.id.editTextUsername), "Username must be 3-15 characters")
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError(findViewById(R.id.editTextEmail), "Invalid email format")
                return false
            }
            phone.length != 10 || !phone.matches(Regex("[0-9]+")) -> {
                showError(findViewById(R.id.ph), "Phone number must be 10 digits")
                return false
            }
            password.length < 8 -> {
                showError(findViewById(R.id.editTextPassword), "Password must be at least 8 characters")
                return false
            }
            !password.matches(Regex(".*[A-Z].*")) ||
                    !password.matches(Regex(".*[a-z].*")) ||
                    !password.matches(Regex(".*[0-9].*")) ||
                    !password.matches(Regex(".*[!@#\$%^&*()_+=|<>{}\\[\\]~-].*")) -> {
                showError(
                    findViewById(R.id.editTextPassword),
                    "Password must include uppercase, lowercase, digit, and special character"
                )
                return false
            }
            else -> return true
        }
    }

    private fun showError(editText: EditText, message: String) {
        editText.error = message
        editText.requestFocus()
    }

    private fun registerUser(username: String, email: String, password: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    saveUserData(userId, username, email, phone)
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(userId: String, username: String, email: String, phone: String) {
        val userMap = hashMapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "phone" to phone
        )

        firestore.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginPage::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
            }
    }
}
