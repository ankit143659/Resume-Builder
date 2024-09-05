package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val  navView : BottomNavigationView=findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController : NavController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }

    @SuppressLint("MissingSuperCall", "MissingInflatedId")
    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.log_out,null)
        dialog.setView(dialogView)

        val yes : Button = dialogView.findViewById(R.id.yes)
        val no : Button = dialogView.findViewById(R.id.no)
        val t1 : TextView = dialogView.findViewById(R.id.t1)

        t1.text = "Are you sure you want to exit ?"

        val alertBox = dialog.create()

        yes.setOnClickListener{
           finishAffinity()
        }

        no.setOnClickListener{
            alertBox.dismiss()
        }

        alertBox.show()
    }

}