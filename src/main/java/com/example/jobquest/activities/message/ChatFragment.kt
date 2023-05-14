package com.example.jobquest.activities.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.adapters.MassageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var massageBox: EditText
    private lateinit var sentButton: ImageView
    private lateinit var massageAdapter: MassageAdapter
    private lateinit var massageModelList: ArrayList<MassageModel>
    private lateinit var mDbRef: DatabaseReference

    //create a private space for sender and receiver
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_chat, container, false)

        val name = requireArguments().getString("name").toString()
        val receiverUid = requireArguments().getString("uid").toString()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        //creating rooms
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        requireActivity().title = name

        chatRecyclerView = view.findViewById(R.id.chatRecyclerview)
        massageBox = view.findViewById(R.id.massageBox)
        sentButton = view.findViewById(R.id.sentButton)
        massageModelList = ArrayList()
        massageAdapter = MassageAdapter(requireContext(), massageModelList)

        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = massageAdapter

        //logic for adding data to recycler view
        mDbRef.child("chats").child(senderRoom!!).child("massages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    massageModelList.clear()

                    for (postSnapshot in snapshot.children){

                        val massageModel = postSnapshot.getValue(MassageModel::class.java)
                        massageModelList.add(massageModel!!)

                    }
                    massageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding the massage to database
        sentButton.setOnClickListener{

            val massage = massageBox.text.toString()
            val massageModelObject = MassageModel(massage,senderUid)
            //val massageModelObject2 = MassageModel(massage,receiverUid)

            mDbRef.child("chats").child(senderRoom!!).child("massages").push()
                .setValue(massageModelObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("massages").push()
                        .setValue(massageModelObject)
                }
            massageBox.setText("")
        }

        return view
    }
}
