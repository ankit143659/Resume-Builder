package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.ResumeAdapter
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class home_Main : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var share: SharePrefrence
    private lateinit var adapter: ResumeAdapter
    private var user_id by Delegates.notNull<Long>()
    private lateinit var db: SQLiteHelper
    private lateinit var recycler: RecyclerView
    private val resumes = mutableListOf<Resume_data>()
    private lateinit var imageContainer : LinearLayout

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home__main, container, false)
        val btn: Button = view.findViewById(R.id.btn_create_cv)
        recycler = view.findViewById(R.id.recyclerView)
        imageContainer = view.findViewById(R.id.imageContainer)
        db = SQLiteHelper(requireContext())
        share = SharePrefrence(requireContext())
        recycler.visibility = View.GONE
        imageContainer.visibility = View.VISIBLE

        share.storeUpdateMode(false)

        try {
            user_id = share.getuser_id().toLong()
        } catch (e: Exception) {
            Log.e("home_Main", "Error getting user ID", e)
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show()
            return view
        }

        recycler.visibility = View.VISIBLE
        imageContainer.visibility = View.GONE

        adapter = ResumeAdapter(resumes,{resumeId ->
            deleteResume(resumeId)},{resumeId,resumeName,createDate->
                onclick(resumeId,resumeName,createDate)
        },{resumeId ->
            editResume(resumeId)
        })
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        loadresume()

        if (user_id <= 0) {
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show()
            return view
        }

        btn.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.resume_info, null)
            dialog.setView(dialogView)

            val create: Button = dialogView.findViewById(R.id.create)
            val cancel: Button = dialogView.findViewById(R.id.no)
            val name: EditText = dialogView.findViewById(R.id.et1)
            val date: TextView = dialogView.findViewById(R.id.et2)
            val alertDialog = dialog.create()

            date.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(), { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, month, dayOfMonth)
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        date.text = dateFormat.format(selectedDate.time)
                    }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }

            create.setOnClickListener {
                val nameText = name.text.toString()
                val dateText = date.text.toString()

                if (nameText.isNotEmpty() && dateText.isNotEmpty()) {
                    val value: Long = db.insertResume(user_id, nameText, dateText)
                    if (value > 0) {
                        val newResume = Resume_data(
                            id = value.toString(),
                            name = nameText,
                            creationDate = dateText
                        )
                       GlobalScope.launch {
                           withContext(Dispatchers.Main) {
                               adapter.addResume(newResume)
                           }
                       }
                        share.storeResumeDetails(nameText,dateText)
                        val intent = Intent(activity, Create_resume::class.java)
                        share.storeResumeId(value)
                        startActivity(intent)
                        alertDialog.dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to create Resume",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill required details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            cancel.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
        return view
    }

    private fun editResume(resumeId: String) {
        val resume_id = resumeId.toLong()
        share.storeResumeId(resume_id)
        share.storeUpdateMode(true)
        val intent = Intent(requireContext(),Create_resume::class.java)
        startActivity(intent)
    }

    private fun onclick(resumeId: String,resumeName : String, createDate: String) {
        val resumeId = resumeId.toLong()
        share.storeResumeId(resumeId)
        share.storeResumeDetails(resumeName,createDate)
        share.storeTemplateName(db.getTemplateName(resumeId))
        val i = Intent(requireContext(),Preview_template::class.java)
        startActivity(i)

    }


    private fun deleteResume(resumeid: String) {

        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.log_out,null)
        dialog.setView(dialogView)

        val yes : Button = dialogView.findViewById(R.id.yes)
        val no : Button = dialogView.findViewById(R.id.no)
        val t1 : TextView = dialogView.findViewById(R.id.t1)

        t1.text = "Are you sure you want to delete this resume ?"

        val alertBox = dialog.create()

        yes.setOnClickListener{

            GlobalScope.launch{
                val result = db.deleteResume(resumeid)
                if (result>0){
                    withContext(Dispatchers.Main){
                        adapter.removeResume(resumeid)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(requireContext(),"Resume removed Succesfully",Toast.LENGTH_SHORT).show()
                    }
                    alertBox.dismiss()
                }else{
                    Toast.makeText(requireContext(),"Failed to delete resume",Toast.LENGTH_SHORT).show()
                }
            }

        }

        no.setOnClickListener{
            alertBox.dismiss()
        }

        alertBox.show()




    }

    private fun loadresume() {
        GlobalScope.launch {
            val allresumes = db.getAllResumes(user_id)
            if (allresumes.count()>0){
                recycler.visibility = View.VISIBLE
                imageContainer.visibility = View.GONE
                withContext(Dispatchers.Main){
                    resumes.clear()
                    resumes.addAll(allresumes.reversed())
                    adapter.notifyDataSetChanged()
                }
            }else{
                recycler.visibility = View.GONE
                imageContainer.visibility = View.VISIBLE
            }
        }
    }
}
