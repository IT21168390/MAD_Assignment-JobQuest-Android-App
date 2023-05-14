package com.example.jobquest.activities.job_management

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.jobquest.models.JobModel
import com.example.jobquest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class JobDetailsActivity : AppCompatActivity(){
    private lateinit var tvJobId: TextView
    private lateinit var tvJobName: TextView
    private lateinit var tvJobDuration: TextView
    private lateinit var tvJobCategory: TextView
    private lateinit var tvjobDescription: TextView
    private lateinit var tvjobLocation: TextView
    private lateinit var tvjobBudget: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        initView()
        setValuesToViews()

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("jobId").toString()
            )
        }

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("jobId").toString(),
                intent.getStringExtra("jobName").toString()
            )
        }


    }

    private fun deleteRecord(id: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Job data deleted", Toast.LENGTH_LONG).show()

            val fragment = FetchingFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    // Update Part
    private fun openUpdateDialog(jobId: String, jobName: String) {
        setContentView(R.layout.update_dialog)

        val etJobName = findViewById<EditText>(R.id.et_job_titleU)
        val etJobBudget = findViewById<EditText>(R.id.et_job_budgetU)
        val etJobCategory = findViewById<Spinner>(R.id.spin_job_categoryU)
        val etJobDurationType = findViewById<Spinner>(R.id.spin_job_durationU)
        val etJobLocation = findViewById<EditText>(R.id.et_job_locationU)
        val etJobDescription = findViewById<EditText>(R.id.et_job_descriptionU)
        val etJobDuration = findViewById<EditText>(R.id.et_job_durationU)
        val btnUpdateData = findViewById<Button>(R.id.btn_job_update)

        etJobName.setText(intent.getStringExtra("jobName").toString())
        etJobBudget.setText(intent.getStringExtra("jobBudget").toString())

        if (intent.getStringExtra("jobCategory").toString()=="Other")
            etJobCategory.setSelection(6)
        if (intent.getStringExtra("jobCategory").toString()=="Hotels & Hospitality")
            etJobCategory.setSelection(5)
        if (intent.getStringExtra("jobCategory").toString()=="House/Office Cleaning")
            etJobCategory.setSelection(4)
        if (intent.getStringExtra("jobCategory").toString()=="Constructions")
            etJobCategory.setSelection(3)
        if (intent.getStringExtra("jobCategory").toString()=="Carpentry")
            etJobCategory.setSelection(2)
        if (intent.getStringExtra("jobCategory").toString()=="Painting")
            etJobCategory.setSelection(1)
        /*
        etJobCategory.setSelection(intent.getStringExtra("jobCategory").toString())
        etJobDurationType.setSelection(intent.getStringExtra("jobDurationType").toString())*/
        etJobLocation.setText(intent.getStringExtra("jobLocation").toString())
        etJobDescription.setText(intent.getStringExtra("jobDescription").toString())
        etJobDuration.setText(intent.getStringExtra("jobDuration").toString())

        val actionBar = supportActionBar
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

            Toast.makeText(applicationContext, "Job Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvJobName.text = etJobName.text.toString()
            tvJobDuration.text = etJobBudget.text.toString()
            tvJobCategory.text = etJobLocation.text.toString()
            tvjobDescription.text = etJobDescription.text.toString()

            finish()
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
        auth = Firebase.auth
        val thisUser: String = auth.currentUser?.uid.toString()

        val userRef = FirebaseDatabase.getInstance().getReference("UserProfiles").child(thisUser)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userName = snapshot.child("userName").getValue(String::class.java)

                    val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
                    val jobInfo = JobModel(jobId,userName,thisUser, name, category, budget, durationType, duration, location, description)// location, description
                    dbRef.setValue(jobInfo)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun initView() {
        tvJobId = findViewById(R.id.tvJobId)//tvEmpId
        tvJobName = findViewById(R.id.tvJobName)//tvEmpName
        tvJobDuration = findViewById(R.id.tvJobDuration)//tvEmpAge
        tvJobCategory = findViewById(R.id.tvJobCategory)//tvEmpSalary
        tvjobDescription = findViewById(R.id.tvjobDescription)//tvjobDescription
        tvjobBudget = findViewById(R.id.tvjobBudget)
        tvjobLocation = findViewById(R.id.tvjobLocation)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)


    }

    // End Update Part

    private fun setValuesToViews() {
        tvJobId.text = ":  "+intent.getStringExtra("jobId")
        tvJobName.text = ":  "+intent.getStringExtra("jobName")
        tvJobDuration.text = ":  "+intent.getStringExtra("jobDuration")
        tvJobCategory.text = ":  "+intent.getStringExtra("jobCategory")
        tvjobLocation.text = ":  "+intent.getStringExtra("jobLocation")
        tvjobDescription.text = ":  "+intent.getStringExtra("jobDescription")
        tvjobBudget.text = ":  "+intent.getStringExtra("jobBudget")


    }
}