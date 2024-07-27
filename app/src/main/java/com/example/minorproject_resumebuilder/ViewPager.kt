package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

class ViewPager : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val adapter = Viewpageradaptor(this)
        viewPager.adapter = adapter
    }
}
