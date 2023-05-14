package com.example.jobquest.activities.job_management

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jobquest.models.JobModel
import com.example.jobquest.R
import com.example.jobquest.adapters.JobAdapter
import com.example.jobquest.fragments.JobApplicationFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class JobDetailsFragmentEmployee : Fragment() {
    private lateinit var tvJobId: TextView
    private lateinit var tvJobName: TextView
    private lateinit var tvJobDuration: TextView
    private lateinit var tvJobCategory: TextView
    private lateinit var tvjobDescription: TextView
    private lateinit var tvjobLocation: TextView
    private lateinit var tvjobBudget: TextView
    private lateinit var btnApplyJob: Button

    private lateinit var empName: String
    private lateinit var empId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_job_details_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setValuesToViews()

        btnApplyJob.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("jobId", tvJobId.toString())
                bundle.putString("jobName", tvJobName.text.toString())
                bundle.putString("empId", empId)
                bundle.putString("empName", empName)
                bundle.putString("jobCategory", tvJobCategory.toString())
                bundle.putString("jobBudget", tvjobBudget.text.toString())
                bundle.putString("jobDuration", tvJobDuration.toString())
                bundle.putString("jobLocation", tvjobLocation.toString())
                bundle.putString("jobDescription", tvjobDescription.toString())

                val fragment = JobApplicationFragment()
                fragment.arguments = bundle

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.mainContainer, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

    }

    private fun initView(view: View) {
        tvJobId = view.findViewById(R.id.tvJobId)
        tvJobName = view.findViewById(R.id.tvJobName)
        tvJobDuration = view.findViewById(R.id.tvJobDuration)
        tvJobCategory = view.findViewById(R.id.tvJobCategory)
        tvjobDescription = view.findViewById(R.id.tvjobDescription)
        tvjobLocation = view.findViewById(R.id.tvjobLocation)
        tvjobBudget = view.findViewById(R.id.tvjobBudget)
        btnApplyJob = view.findViewById(R.id.btnApplyJob)

        setValuesToViews()
    }


// End Update Part

    private fun setValuesToViews() {
        val args = requireArguments()
        tvJobId.text = args.getString("jobId")
        tvJobName.text = args.getString("jobName")
        tvJobDuration.text = args.getString("jobDuration")
        tvJobCategory.text = args.getString("jobCategory")
        tvjobLocation.text = args.getString("jobLocation")
        tvjobDescription.text = args.getString("jobDescription")
        tvjobBudget.text = args.getString("jobBudget")
        empId = args.getString("empId").toString()
        empName = args.getString("empName").toString()
    }
}