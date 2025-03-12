package com.example.minorproject_resumebuilder

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.commit
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence

class Settings : Fragment() {

    private lateinit var sharedPreferences: SharePrefrence

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val profile = view.findViewById<TextView>(R.id.profile)
        val themeSwitch = view.findViewById<Switch>(R.id.switchtheme)

        // Load saved theme preference
        sharedPreferences = SharePrefrence(requireContext())



        // Theme switch listener
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.setDarkModeEnabled(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.setDarkModeEnabled(false)
            }
        }

        // Profile navigation
        profile.setOnClickListener {
            val profileFragment = Profile()
            val bundle = Bundle()
            bundle.putString("username", "John Doe") // Example data
            profileFragment.arguments = bundle

            parentFragmentManager.commit {
                replace(R.id.fragment_container, profileFragment)
                addToBackStack(null) // Allows back navigation
            }
        }

        return view
    }
}
