package com.example.minorproject_resumebuilder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class admin_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        // Ensure the view exists before using it
        val someView = findViewById<View>(R.id.customView)

        someView?.setOnApplyWindowInsetsListener { v, insets -> insets }


    }
}
