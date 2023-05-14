package com.example.jobquest.activities.job_management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.jobquest.models.JobModel
import com.example.jobquest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class InsertionFragment : Fragment() {
    private lateinit var job_name: EditText
    private lateinit var job_category: Spinner
    private lateinit var job_budget: EditText
    private lateinit var job_duration: EditText
    private lateinit var job_location: EditText
    private lateinit var job_description: EditText
    private lateinit var post_job: Button
    private lateinit var dbRef: DatabaseReference

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_insertion, container, false)

        job_name = view.findViewById(R.id.et_job_title)
        job_category = view.findViewById(R.id.spin_job_category)
        job_budget = view.findViewById(R.id.et_job_budget)
        job_duration = view.findViewById(R.id.et_job_duration)
        job_location = view.findViewById(R.id.et_job_location)
        job_description = view.findViewById(R.id.et_job_description)
        post_job = view.findViewById(R.id.btn_job_post)


        dbRef = FirebaseDatabase.getInstance().getReference("Jobs")

        post_job.setOnClickListener {
            saveJobData()
        }


        return view
    }

    private fun saveJobData() {

        auth = Firebase.auth
        val thisUserUid: String = auth.currentUser?.uid.toString()
        lateinit var userName: String

        val userRef = FirebaseDatabase.getInstance().getReference("UserProfiles").child(thisUserUid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.child("userName").getValue(String::class.java).toString()

                    // Move the code that depends on the value of userName here
                    val employer = userName
                    val employerId = thisUserUid
                    val name = job_name.text.toString()
                    val category = job_category.selectedItem.toString()
                    val budget = job_budget.text.toString()
                    val duration = job_duration.text.toString()
                    val location = job_location.text.toString()
                    val description = job_description.text.toString()

                    if (name.isEmpty()) {
                        job_name.error = "Please enter job name"
                        return
                    }
                    if (budget.isEmpty()) {
                        job_budget.error = "Please enter job name"
                        return
                    }
                    if (duration.isEmpty()) {
                        job_duration.error = "Please enter job duration"
                        return
                    }
                    if (location.isEmpty()) {
                        job_location.error = "Please enter job location"
                        return
                    }
                    if (description.isEmpty()) {
                        job_description.error = "Please enter job description"
                        return
                    }

                    val jobId = dbRef.push().key!!

                    val job = JobModel(jobId, employer, employerId, name, category, budget,"", duration, location, description)

                    dbRef.child(jobId).setValue(job)
                        .addOnCompleteListener {
                            Toast.makeText(activity, "Data inserted successfully", Toast.LENGTH_LONG).show()

                            job_name.text.clear()
                            job_budget.text.clear()
                            job_duration.text.clear()
                            job_location.text.clear()
                            job_description.text.clear()

                        }.addOnFailureListener { err ->
                            Toast.makeText(activity, "Error ${err.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}
