package com.example.minorproject_resumebuilder.com.example.minorproject_resumebuilder

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject_resumebuilder.R
import com.example.minorproject_resumebuilder.Resume_data

class ResumeAdapter(
    private val resumes: MutableList<Resume_data>,
    private val onDelete: (String) -> Unit,
    private val onClick : (String)->Unit
) : RecyclerView.Adapter<ResumeAdapter.ViewHolder>() {

    private lateinit var share : SharePrefrence
    private lateinit var db : SQLiteHelper

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resumeName: TextView = itemView.findViewById(R.id.resume_name)
        val createDate : TextView = itemView.findViewById(R.id.create_date)
        val resumeId : TextView = itemView.findViewById(R.id.resume_id)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        //val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resumes_layout_home_screen, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val resume = resumes[position]
        holder.resumeId.text = resume.id
        holder.resumeName.text = resume.name
        holder.createDate.text = resume.creationDate


        holder.deleteButton.setOnClickListener {
            onDelete(resume.id)
        }

        holder.itemView.setOnClickListener {
            onClick(resume.id)
        }

    }

    override fun getItemCount() = resumes.size

    fun removeResume(resumeId : String){
        val Position = resumes.indexOfFirst { it.id == resumeId}
        if (Position != -1) {
            resumes.removeAt(Position)
            notifyItemRemoved(Position)
        }
    }

    fun addResume(resume: Resume_data) {
        val position = resumes.indexOfFirst { it.id > resume.id }

        if (position == -1) {
            resumes.add(resume)
            notifyItemInserted(resumes.size - 1)
        } else {
            resumes.add(position, resume)
            notifyItemInserted(position)
        }

    }

}
