package com.example.jobquest.activities.job_management

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.models.JobModel
import com.example.jobquest.R
import com.example.jobquest.adapters.JobAdapter
import com.google.firebase.database.*

class FetchingFragment : Fragment() {
    private lateinit var jobRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var jobList: ArrayList<JobModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_fetching, container, false)
        jobRecyclerView = root.findViewById(R.id.rvEmp)
        jobRecyclerView.layoutManager = LinearLayoutManager(context)
        jobRecyclerView.setHasFixedSize(true)
        tvLoadingData = root.findViewById(R.id.tvLoadingData)
        jobList = arrayListOf<JobModel>()
        return root
    }

    override fun onStart() {
        super.onStart()
        getJobsData()
    }

    private fun getJobsData() {
        jobRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Jobs")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()
                if (snapshot.exists()){
                    for (jobSnap in snapshot.children){
                        val jobData = jobSnap.getValue(JobModel::class.java)
                        jobList.add(jobData!!)
                    }
                    val mAdapter = JobAdapter(jobList)
                    jobRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : JobAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val bundle = Bundle()
                            bundle.putString("jobId", jobList[position].jobId)
                            bundle.putString("empName", jobList[position].empName)
                            bundle.putString("empId", jobList[position].empId)
                            bundle.putString("jobName", jobList[position].jobName)
                            bundle.putString("jobCategory", jobList[position].jobCategory)
                            bundle.putString("jobBudget", jobList[position].jobBudget)
                            bundle.putString("jobDurationType", jobList[position].jobDurationType)
                            bundle.putString("jobDuration", jobList[position].jobDuration)
                            bundle.putString("jobLocation", jobList[position].jobLocation)
                            bundle.putString("jobDescription", jobList[position].jobDescription)

                            val intent = Intent(requireActivity(), JobDetailsActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }

                    })

                    jobRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }

        })
    }
}
