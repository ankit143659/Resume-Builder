package com.example.minorproject_resumebuilder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Loading_page : AppCompatActivity() {

    private lateinit var prefrence: SharePrefrence
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading_page)


        prefrence = SharePrefrence(this)

        if (prefrence.isDarkModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        Handler(Looper.getMainLooper()).postDelayed({
            if(prefrence.isLoggedIn()){
                val I=Intent(this,HomePage::class.java)
                startActivity(I)
                finish()
            }
            else{
                val I=Intent(this,MainActivity::class.java)
                startActivity(I)
                finish()
            }

        },3000)
    }
}