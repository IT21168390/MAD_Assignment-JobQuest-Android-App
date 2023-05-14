package com.example.jobquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jobquest.R
import com.example.jobquest.activities.message.MainActivityMsgFragment
import com.example.jobquest.models.NewApplicationsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class EmployeeHomeFragment : Fragment(R.layout.fragment_employee_home) {

    private lateinit var dbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var txtWelcomeLine: TextView
    var acceptedCount = 0
    var sentApplicationsAmount = 0
    private lateinit var txtSentApplicationsCount: TextView
    private lateinit var txtAcceptedApplicationsCount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_home, container, false)
    }

    companion object {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acceptedCount = 0
        sentApplicationsAmount = 0

        txtWelcomeLine = view.findViewById(R.id.employeeHomeWelcome)
        txtSentApplicationsCount = view.findViewById(R.id.textViewApplicationsCountValue)
        txtAcceptedApplicationsCount = view.findViewById(R.id.textViewAcceptedAmountValue)
        // Rest of code
        getJobApplicationsData()

        val viewStatusButton = view.findViewById<View>(R.id.btnViewSentApplications)
        viewStatusButton.setOnClickListener {
            val nextFragment = SentApplicationsFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val contactButton = view.findViewById<View>(R.id.btnContactFunction)
        contactButton.setOnClickListener {
            val nextFragment = MainActivityMsgFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    private fun getJobApplicationsData(){
        auth = Firebase.auth
        val thisUserUid: String = auth.currentUser?.uid.toString()

        dbReference = FirebaseDatabase.getInstance().getReference("JobApplications")
        val query = dbReference.orderByChild("userId").equalTo(thisUserUid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (jobApps in snapshot.children){
                        val applicationData = jobApps.getValue(NewApplicationsModel::class.java)
                        if (applicationData?.status == "ACCEPTED") {
                            acceptedCount++
                        }
                        sentApplicationsAmount++
                    }

                    txtSentApplicationsCount.text = sentApplicationsAmount.toString()
                    txtAcceptedApplicationsCount.text = acceptedCount.toString()

                    // Retrieve the user name
                    val userRef = FirebaseDatabase.getInstance().getReference("UserProfiles").child(thisUserUid)
                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val userName = snapshot.child("userName").getValue(String::class.java)
                                txtWelcomeLine.text = "Welcome $userName!"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        dbReference = FirebaseDatabase.getInstance().getReference("UserProfiles").child(thisUserUid)
    }
}