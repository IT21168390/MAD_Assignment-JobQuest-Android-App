package com.example.jobquest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.jobquest.activities.LogOutActivity
import com.example.jobquest.activities.job_management.*
import com.example.jobquest.fragments.EmployeeHomeFragment
import com.example.jobquest.fragments.ReceivedJobApplicationsFragment
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.fragments.JobApplicationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val sentApplicationsFragment = SentApplicationsFragment()
        val jobApplication = JobApplicationFragment()
        val receivedApplications = ReceivedJobApplicationsFragment()
        val employeeHomeFragment = EmployeeHomeFragment()

        val employerHome = FetchingFragment()
        val jobPost = InsertionFragment()
        val employersPostedJobsViewForEmployee = FetchingFragmentEmployee()


        val intentLogOut = Intent(this, LogOutActivity::class.java)


        auth = Firebase.auth
        val thisUser: String = auth.currentUser?.uid.toString()
        dbReference = FirebaseDatabase.getInstance().getReference("UserProfiles").child(thisUser)
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentUserType = dataSnapshot.child("userType").getValue(String::class.java)

                if (currentUserType == "Employer") {
                    setCurrentFragment(receivedApplications)

                    bottomNavigationView.setOnItemSelectedListener {
                        when (it.itemId) {
                            R.id.profileNav -> setCurrentFragment(jobPost)
                            R.id.exploreNav -> setCurrentFragment(receivedApplications)
                            R.id.homeNav -> setCurrentFragment(employerHome)
                            R.id.logInNav -> startActivity(intentLogOut)
                        }
                        true
                    }
                } else{
                    setCurrentFragment(sentApplicationsFragment)
                    bottomNavigationView.setOnItemSelectedListener {
                        when (it.itemId) {
                            R.id.profileNav -> setCurrentFragment(sentApplicationsFragment)
                            R.id.exploreNav -> setCurrentFragment(employersPostedJobsViewForEmployee)
                            R.id.homeNav -> setCurrentFragment(employeeHomeFragment)
                            R.id.logInNav -> startActivity(intentLogOut)
                        }
                        true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainContainer, fragment)
            commit()
        }
}