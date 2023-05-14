package com.example.jobquest.activities.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.models.NewUserProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivityMsgFragment : Fragment(R.layout.activity_mainmsg) {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<NewUserProfileModel>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_mainmsg, container, false)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)
        userRecyclerView = view.findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter
        mDbRef.child("UserProfiles").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    var currentUser = postSnapshot.getValue(NewUserProfileModel::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.UserID){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }
}
