package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class Create : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view :View= inflater.inflate(R.layout.fragment_create, container, false)

        val Btn : LinearLayout = view.findViewById(R.id.personal)
        Btn.setOnClickListener {
            val intent = Intent(activity, Basic_personal_details::class.java)
            startActivity(intent)
        }

        return view
    }
}