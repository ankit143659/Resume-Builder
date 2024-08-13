package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class home_Main : Fragment(R.layout.fragment_home__main) {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home__main, container, false)
        val Btn : Button = view.findViewById(R.id.btn_create_cv)
        Btn.setOnClickListener {
            val dailog = AlertDialog.Builder(requireContext())
            val dailogView = LayoutInflater.from(requireContext()).inflate(R.layout.resume_info,null)

            dailog.setView(dailogView)
            val create : Button= dailogView.findViewById(R.id.create)
            val cancle : Button= dailogView.findViewById(R.id.no)
            val name : EditText = dailogView.findViewById(R.id.et1)
            val date: EditText = dailogView.findViewById(R.id.et2)

            val alertDialog = dailog.create()
            create.setOnClickListener{
                if(name.length()!=0)
                {
                    val intent = Intent(activity,Basic_personal_details::class.java)
                    startActivity(intent)
                    alertDialog.dismiss()
                }
                else{
                    name.setError("Name required")
                }
            }

            cancle.setOnClickListener{
                alertDialog.dismiss()
            }

        }
        return view
    }


}