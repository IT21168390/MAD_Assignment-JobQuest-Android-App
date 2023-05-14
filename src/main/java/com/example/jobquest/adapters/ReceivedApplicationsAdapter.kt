package com.example.jobquest.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.activities.job_management.FetchingFragment
import com.example.jobquest.activities.message.ChatFragment
import com.example.jobquest.fragments.ReceivedJobApplicationsFragment
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.models.NewApplicationsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReceivedApplicationsAdapter(private val applications: ArrayList<NewApplicationsModel>, private val receivedAppFragment: ReceivedJobApplicationsFragment) :
    RecyclerView.Adapter<ReceivedApplicationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.received_application_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applications[position]
        holder.bind(application)
    }

    override fun getItemCount() = applications.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_textview)
        private val paymentTypeTextView: TextView = itemView.findViewById(R.id.payment_type_textview)

        private val estimatedCostForJob:TextView = itemView.findViewById(R.id.expectedRate_textview)
        private val statusSpinner: Spinner = itemView.findViewById(R.id.status_spinner)
        private val cancelButton: Button = itemView.findViewById(R.id.cancel_button)
        private val updateButton: Button = itemView.findViewById(R.id.updateStatus_button)
        private val proceedButton: Button = itemView.findViewById(R.id.proceed_button)

        private val appliedJobTitle: TextView = itemView.findViewById(R.id.job_tv)
        private val ageTextView: TextView = itemView.findViewById(R.id.age_textview)
        private val cityTextView: TextView = itemView.findViewById(R.id.city_textview)

        fun bind(application: NewApplicationsModel) {
            nameTextView.text = application.name
            paymentTypeTextView.text = application.paymentType
            if (application.expectedSalary != ""){
                estimatedCostForJob.text = application.expectedSalary.toString()
            }
            else{
                estimatedCostForJob.text = application.chargingRate.toString()
            }
            appliedJobTitle.text = "Job: "+application.jobTitle
            ageTextView.text = "Age: "+application.age.toString()+" Years Old"
            cityTextView.text = application.city

            if (application.status=="ACCEPTED"){
                statusSpinner.setSelection(1)
            }
            else{
                statusSpinner.setSelection(0)
            }

            cancelButton.setOnClickListener {
                // TODO: Handle cancel button click
                deleteJobApplication(application.applicationId)
                //This cast can be never succeed:- (itemView.context as SentApplicationsFragment).refreshSentApplicationsUI()
                receivedAppFragment.refreshReceivedApplicationsUI()
            }

            updateButton.setOnClickListener {
                updateApplicationStatus(application.applicationId, statusSpinner.selectedItem.toString().uppercase())

                Toast.makeText(itemView.context, "Application Status Updated.", Toast.LENGTH_SHORT).show()
                receivedAppFragment.refreshReceivedApplicationsUI()
            }

            proceedButton.setOnClickListener {
                proceedJobApplication(application.applicationId)
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
        //
        private fun updateApplicationStatus(applicationId:String?, statusSpinner:String){
            val dbReference = FirebaseDatabase.getInstance().getReference("JobApplications").child(applicationId!!)
            val update = HashMap<String, Any>()
            update["status"] = statusSpinner
            dbReference.updateChildren(update)
        }

        private fun proceedJobApplication(applicationId:String?){
            // Retrieve the user name
            val dbReference = FirebaseDatabase.getInstance().getReference("JobApplications").child(applicationId!!)
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val sendToUSERID = snapshot.child("userId").getValue(String::class.java)

                        val fragment = ChatFragment()
                        val bundle = Bundle()
                        bundle.putString("name", nameTextView.text.toString())
                        bundle.putString("uid", sendToUSERID)
                        fragment.arguments = bundle

                        val context = itemView.context
                        if (context is AppCompatActivity) {
                            // Use the activity context to get the supportFragmentManager
                            val fragmentManager = context.supportFragmentManager
                            val transaction = fragmentManager.beginTransaction()
                            transaction.replace(R.id.mainContainer, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }


    }
}
