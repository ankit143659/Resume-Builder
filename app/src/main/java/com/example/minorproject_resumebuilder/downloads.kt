package com.example.minorproject_resumebuilder

import ResumeDownloadAdaptor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SQLiteHelper
import com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder.SharePrefrence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class downloads : Fragment() {

    private lateinit var db: SQLiteHelper
    private lateinit var share: SharePrefrence
    private lateinit var adapter: ResumeDownloadAdaptor
    private lateinit var recycler: RecyclerView
    private val resumes = mutableListOf<downLoadedData>()
    private var user_id by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_downloads, container, false)

        // Initialize SharePreference and SQLiteHelper
        share = SharePrefrence(requireContext())
        db = SQLiteHelper(requireContext())

        // Set up RecyclerView
        recycler = view.findViewById(R.id.recyclerView)
        adapter = ResumeDownloadAdaptor(resumes,{ resumeId ->
            deleteResume(resumeId)
        })
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Fetch the user ID from shared preferences
        try {
            user_id = share.getuser_id().toLong()
        } catch (e: Exception) {
            Log.e("Download_section", "Error getting user ID", e)
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show()
            return view
        }

        // Load data from the database
        loadData()

        return view
    }

    private fun deleteResume(resumeId: String) {
        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.log_out, null)
        dialog.setView(dialogView)

        val yes: Button = dialogView.findViewById(R.id.yes)
        val no: Button = dialogView.findViewById(R.id.no)
        val t1: TextView = dialogView.findViewById(R.id.t1)

        t1.text = "Are you sure you want to delete this Downloaded resume ?"

        val alertBox = dialog.create()

        yes.setOnClickListener {

            GlobalScope.launch {
                val result = db.deleteDownloadedResume(resumeId.toLong())
                if (result) {
                    withContext(Dispatchers.Main) {
                        adapter.removeResume(resumeId)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(
                            requireContext(),
                            "Resume removed Succesfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    alertBox.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete resume", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        no.setOnClickListener{
            alertBox.dismiss()
        }
        alertBox.show()
    }

    private fun loadData() {
        // Use lifecycleScope to ensure coroutines are canceled when the Fragment is destroyed
        lifecycleScope.launch {
            try {
                val allDownloads = withContext(Dispatchers.IO) { db.getAllDownloaded(share.getuser_id().toLong()) }
                if (allDownloads.isNotEmpty()) {
                    recycler.visibility = View.VISIBLE
                    resumes.clear()
                    resumes.addAll(allDownloads.reversed()) // Show the latest first
                    adapter.notifyDataSetChanged()
                } else {
                    recycler.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("Download_section", "Error loading downloaded data", e)
                Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
