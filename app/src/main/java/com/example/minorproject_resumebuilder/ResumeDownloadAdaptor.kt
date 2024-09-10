import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject_resumebuilder.R
import com.example.minorproject_resumebuilder.downLoadedData

class ResumeDownloadAdaptor(
    private val resumeList: MutableList<downLoadedData>,
    private val onDelete: (String) -> Unit) :
    RecyclerView.Adapter<ResumeDownloadAdaptor.ResumeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.downloadlayout, parent, false)
        return ResumeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = resumeList[position]
        holder.nameTextView.text = resume.resumeName
        holder.dateTextView.text = resume.createDate
        holder.deleteButton.setOnClickListener{
            onDelete(resume.id.toString())
        }
    }

    fun removeResume(resumeId : String){
        val Position = resumeList.indexOfFirst { it.id.toString() == resumeId}
        if (Position != -1) {
            resumeList.removeAt(Position)
            notifyItemRemoved(Position)
        }
    }
    override fun getItemCount(): Int = resumeList.size

    // ViewHolder class to hold views for each item
    inner class ResumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.resume_name)
        val dateTextView: TextView = itemView.findViewById(R.id.create_date)
        val deleteButton : Button = itemView.findViewById(R.id.deleteButton)
    }
}
