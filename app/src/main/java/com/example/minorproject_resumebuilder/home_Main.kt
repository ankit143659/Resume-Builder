package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import java.text.SimpleDateFormat
import java.util.*

class home_Main : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var share : SharePrefrence
    private lateinit var user_id : String
    private lateinit var db : SQLiteHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home__main, container, false)
        val Btn : Button = view.findViewById(R.id.btn_create_cv)
        share = SharePrefrence(requireContext())
        user_id= share.getuser_id()
        db = SQLiteHelper(requireContext())
        val User_id = user_id.toLong()
        
        Btn.setOnClickListener {
            val dailog = AlertDialog.Builder(requireContext())
            val dailogView = LayoutInflater.from(requireContext()).inflate(R.layout.resume_info,null)

            dailog.setView(dailogView)
            val create : Button= dailogView.findViewById(R.id.create)
            val cancle : Button= dailogView.findViewById(R.id.no)
            val name : EditText = dailogView.findViewById(R.id.et1)
            val date : TextView = dailogView.findViewById(R.id.et2)
            val alertDialog = dailog.create()

            date.setOnClickListener{
                val datepicker = DatePickerDialog(requireContext(),{ _,year,month,dayOfmonth ->
                    val selectDate = Calendar.getInstance()
                    selectDate.set(year,month,dayOfmonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    date.setText(dateFormat.format(selectDate.time))
                }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datepicker.show()
            }
            val Name = name.toString()
            
            val Date = date.toString()
            create.setOnClickListener{
                if(name.length()!=0 && date.length()!=0)
                {
                    val value = db.insertResume(User_id,Name,Date)
                    if (value!=null){
                        val intent = Intent(activity,Create_resume::class.java).apply{
                            putExtra("resume_id",value)
                        }
                        startActivity(intent)
                        alertDialog.dismiss()
                    }else{
                        Toast.makeText(requireContext(),"")
                    }
                    

                }
                else{
                    Toast.makeText(requireContext(),"Please fill require details",Toast.LENGTH_SHORT).show()
                }
            }

            cancle.setOnClickListener{
                alertDialog.dismiss()
            }

            alertDialog.show()


        }
        return view
    }


}