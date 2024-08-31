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
import java.text.SimpleDateFormat
import java.util.*

class home_Main : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var share: SharePrefrence
    private lateinit var user_id: String
    private lateinit var db: SQLiteHelper

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home__main, container, false)
        val btn: Button = view.findViewById(R.id.btn_create_cv)
        share = SharePrefrence(requireContext())
        user_id = share.getuser_id()
        db = SQLiteHelper(requireContext())
        val userId = user_id.toLong()

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
                val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
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
                    val value = db.insertResume(userId, nameText, dateText)
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
