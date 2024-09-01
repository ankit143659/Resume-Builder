package com.example.minorproject_resumebuilder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlin.properties.Delegates

class home_Main : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var share: SharePrefrence
    private var user_id by Delegates.notNull<Long>()
    private lateinit var db: SQLiteHelper

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home__main, container, false)
        val btn: Button = view.findViewById(R.id.btn_create_cv)

        // Initialize SharePrefrence and get the user_id
        share = SharePrefrence(requireContext())
        try {
            user_id = share.getuser_id().toLong()
        } catch (e: Exception) {
            Log.e("home_Main", "Error getting user ID", e)
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show()
            return view
        }

        // Initialize SQLiteHelper
        db = SQLiteHelper(requireContext())

        if (user_id <= 0) {
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show()
            return view
        }

        btn.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.resume_info, null)
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
                        val intent = Intent(activity, Create_resume::class.java).apply {
                            putExtra("resume_id", value)
                        }
                        startActivity(intent)
                        alertDialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed to create Resume", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill required details", Toast.LENGTH_SHORT).show()
                }
            }

            cancel.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        return view
    }
}
