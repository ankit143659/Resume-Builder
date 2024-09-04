package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject_resumebuilder.R
import com.example.minorproject_resumebuilder.Resume_data

class ResumeAdapter(
    private val resumes: List<Resume_data>,
    //private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<ResumeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resumeName: TextView = itemView.findViewById(R.id.resume_name)
        val createDate : TextView = itemView.findViewById(R.id.create_date)
        //val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        //val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resumes_layout_home_screen, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumes[position]
        holder.resumeName.text = resume.name
        holder.createDate.text = resume.creationDate

    }

    override fun getItemCount() = resumes.size
}
