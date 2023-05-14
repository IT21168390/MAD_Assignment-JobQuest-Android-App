package com.example.jobquest.activities.job_management

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jobquest.models.JobModel
import com.example.jobquest.R
import com.google.firebase.database.FirebaseDatabase

class JobDetailsFragment : Fragment() {

    private lateinit var tvJobId: TextView
    private lateinit var tvJobName: TextView
    private lateinit var tvJobDuration: TextView
    private lateinit var tvJobCategory: TextView
    private lateinit var tvjobDescription: TextView
    private lateinit var tvjobLocation: TextView
    private lateinit var tvjobBudget: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_job_details, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        /*setValuesToViews()*/

        btnDelete.setOnClickListener {
            deleteRecord(
                requireArguments().getString("jobId").toString()
            )
        }

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                requireArguments().getString("jobId").toString(),
                requireArguments().getString("jobName").toString()
            )
        }
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(requireContext(), "Job data deleted", Toast.LENGTH_LONG).show()

            val fetchingFragment = FetchingFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, fetchingFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }.addOnFailureListener { error ->
            Toast.makeText(requireContext(), "Deleting Err ${error.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    // Update Part
    private fun openUpdateDialog(jobId: String, jobName: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.update_dialog, null)
        if (view == null) {
            Log.e(TAG, "Failed to inflate update_dialog.xml layout")
            return
        }
        //val view = LayoutInflater.from(requireContext()).inflate(R.layout.update_dialog, null)

        val etJobName = view.findViewById<EditText>(R.id.et_job_titleU)
        val etJobBudget = view.findViewById<EditText>(R.id.et_job_budgetU)
        val etJobCategory = view.findViewById<Spinner>(R.id.spin_job_categoryU)
        val etJobDurationType = view.findViewById<Spinner>(R.id.spin_job_durationU)
        val etJobLocation = view.findViewById<EditText>(R.id.et_job_locationU)
        val etJobDescription = view.findViewById<EditText>(R.id.et_job_descriptionU)
        val etJobDuration = view.findViewById<EditText>(R.id.et_job_durationU)
        val btnUpdateData = view.findViewById<Button>(R.id.btn_job_update)

        etJobName.setText(requireArguments().getString("jobName").toString())
        etJobBudget.setText(requireArguments().getString("jobBudget").toString())
        //        etJobCategory.setSelection(intent.getStringExtra("jobBudget").toString())
        //        etJobDurationType.setSelection(intent.getStringExtra("jobBudget").toString())
        etJobLocation.setText(requireArguments().getString("jobLocation").toString())
        etJobDescription.setText(requireArguments().getString("jobDescription").toString())
        etJobDuration.setText(requireArguments().getString("jobDuration").toString())

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = "Updating $jobName Record"

        btnUpdateData.setOnClickListener {
            updateEmpData(
                jobId,
                etJobName.text.toString(),
                etJobCategory.getSelectedItem().toString(),
                etJobBudget.text.toString(),
                etJobDurationType.getSelectedItem().toString(),
                etJobDuration.text.toString(),
                etJobLocation.text.toString(),
                etJobDescription.text.toString(),
            )

            Toast.makeText(requireContext(), "Job Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvJobName.text = etJobName.text.toString()
            tvJobDuration.text = etJobBudget.text.toString()
            tvJobCategory.text = etJobLocation.text.toString()
            tvjobDescription.text = etJobDescription.text.toString()

            requireActivity().finish()
        }
    }


    private fun updateEmpData(
        jobId: String,
        name: String,
        category: String,
        budget: String,
        durationType: String,
        duration: String,
        location: String,
        description: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
        val jobInfo = JobModel(
            jobId,
            name,
            category,
            budget,
            durationType,
            duration,
            location,
            description
        )// location, description
        dbRef.setValue(jobInfo)
    }

    private fun initView(view: View) {
        tvJobId = view.findViewById(R.id.tvJobId)
        tvJobName = view.findViewById(R.id.tvJobName)
        tvJobDuration = view.findViewById(R.id.tvJobDuration)
        tvJobCategory = view.findViewById(R.id.tvJobCategory)
        tvjobDescription = view.findViewById(R.id.tvjobDescription)
        tvjobLocation = view.findViewById(R.id.tvjobLocation)
        tvjobBudget = view.findViewById(R.id.tvjobBudget)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        btnDelete = view.findViewById(R.id.btnDelete)

        setValuesToViews()
    }

    private fun setValuesToViews() {
        val args = requireArguments()
        tvJobId.text = args.getString("jobId")
        tvJobName.text = args.getString("jobName")
        tvJobDuration.text = args.getString("jobDuration")
        tvJobCategory.text = args.getString("jobCategory")
        tvjobLocation.text = args.getString("jobLocation")
        tvjobDescription.text = args.getString("jobDescription")
        tvjobBudget.text = args.getString("jobBudget")
    }

}