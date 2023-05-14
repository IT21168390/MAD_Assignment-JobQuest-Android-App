package com.example.jobquest.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.adapters.SentApplicationsAdapter
import com.example.jobquest.models.NewApplicationsModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SentApplicationsFragment : Fragment(R.layout.fragment_sent_job_applications_rv) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SentApplicationsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    // New Lines
    private lateinit var applications: ArrayList<NewApplicationsModel>
    private lateinit var dbReference: DatabaseReference

    private lateinit var auth: FirebaseAuth
    //

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sent_job_applications_rv, container, false)
        recyclerView = view.findViewById(R.id.sentApplicationsRecyclerView)
        getJobApplicationsData()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the profile nav icon for current fragment
        val bottomNavigationView: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation)
        bottomNavigationView?.selectedItemId = R.id.profileNav

        // New Code for Firebase
        applications = arrayListOf<NewApplicationsModel>()
        //getJobApplicationsData()

        layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.sentApplicationsRecyclerView)
        //recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

    }

    // New Code for Firebase
    private val fragmentContext = this@SentApplicationsFragment

    private fun getJobApplicationsData(){
        auth = Firebase.auth
        val thisUserUid: String = auth.currentUser?.uid.toString()

        dbReference = FirebaseDatabase.getInstance().getReference("JobApplications")
        val query = dbReference.orderByChild("userId").equalTo(thisUserUid)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (jobApps in snapshot.children){
                        val applicationData = jobApps.getValue(NewApplicationsModel::class.java)
                        applications.add(applicationData!!)
                    }
                    /* Replaced with the below adapter because SentApplicationsAdapter's parameters were changed for deleteButton method.
                    val newAdapter = SentApplicationsAdapter(applications)*/
                    val newAdapter = SentApplicationsAdapter(applications, fragmentContext)
                    recyclerView.adapter = newAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // Used when deleting a Job Application
     fun refreshSentApplicationsUI(){
        val nextFragment = SentApplicationsFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainContainer, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    //


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}