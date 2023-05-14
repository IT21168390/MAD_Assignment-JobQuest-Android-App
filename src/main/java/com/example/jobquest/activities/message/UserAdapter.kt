package com.example.jobquest.activities.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.jobquest.R
import com.example.jobquest.models.NewUserProfileModel

class UserAdapter(val context: Context, val userList: ArrayList<NewUserProfileModel>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userList[position]

        holder.textName.text = currentUser.userName

        holder.itemView.setOnClickListener{
            val fragment = ChatFragment()
            val bundle = Bundle()
            bundle.putString("name", currentUser.userName)
            bundle.putString("uid", currentUser.UserID)
            fragment.arguments = bundle

            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }

}