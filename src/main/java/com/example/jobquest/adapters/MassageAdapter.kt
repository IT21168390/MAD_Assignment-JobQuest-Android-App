package com.example.jobquest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.jobquest.R
import com.example.jobquest.activities.message.MassageModel
import com.google.firebase.auth.FirebaseAuth

class MassageAdapter(val context: Context, val massageModelList: ArrayList<MassageModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType == 1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMassage = massageModelList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //do stuff for sent view holder
            val  viewHolder = holder as SentViewHolder
            holder.sentMassage.text = currentMassage.massage

        }else{
            //do stuff for receive view holder
            val  viewHolder = holder as ReceiveViewHolder
            holder.receiveMassage.text = currentMassage.massage

        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMassage = massageModelList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMassage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return massageModelList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val  sentMassage = itemView.findViewById<TextView>(R.id.txt_sent_massage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val  receiveMassage = itemView.findViewById<TextView>(R.id.txt_receive_massage)
    }

}