package com.example.jobquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.models.NewApplicationsModel
import com.google.firebase.database.FirebaseDatabase

class SentApplicationsAdapter(private val applications: ArrayList<NewApplicationsModel>, private val sentAppFragment: SentApplicationsFragment) :
    RecyclerView.Adapter<SentApplicationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_application_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applications[position]
        holder.bind(application)
    }

    override fun getItemCount() = applications.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobTitle)
        private val budgetTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobBudget)
        private val posterTextView: TextView = itemView.findViewById(R.id.sentApplicationItemJobPoster)
        private val statusTextView: TextView = itemView.findViewById(R.id.sentApplicationItemStatus)
        private val cancelButton: Button = itemView.findViewById(R.id.sentApplicationItemCancelButton)

        fun bind(application: NewApplicationsModel) {
            titleTextView.text = application.jobTitle
            budgetTextView.text = "Estimated Budget is Rs. "+application.jobBudget
            posterTextView.text = "Employer: "+application.employer
            statusTextView.text = application.status

            if (statusTextView.text.toString() == "ACCEPTED") {
                statusTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            }

            cancelButton.setOnClickListener {
                // TODO: Handle cancel button click
                deleteJobApplication(application.applicationId)
                //This cast can be never succeed:- (itemView.context as SentApplicationsFragment).refreshSentApplicationsUI()
                sentAppFragment.refreshSentApplicationsUI()
            }
        }

        // New Code for Firebase + Delete
        private fun deleteJobApplication(applicationId:String?){
            val dbReference = FirebaseDatabase.getInstance().getReference("JobApplications").child(applicationId!!)
            val dTask = dbReference.removeValue()

            dTask.addOnSuccessListener {
                Toast.makeText(itemView.context, "Application Deleted!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{error->
                Toast.makeText(itemView.context, "Deleting error ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

}