package com.example.minorproject_resumebuilder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Page3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view :View= inflater.inflate(R.layout.fragment_page3, container, false)

        val Btn : Button= view.findViewById(R.id.homebutton)
            Btn.setOnClickListener {
                val intent = Intent(activity, HomePage::class.java)
                startActivity(intent)
                activity?.finish()
        }

        return view
    }

}
