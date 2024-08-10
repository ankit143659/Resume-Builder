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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Create.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            activity?.finish()
        }

        return view
    }
}