package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: MutableList<UserData>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder class to hold references to views in each item
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        val userEmailTextView: TextView = itemView.findViewById(R.id.userEmail)
        val userImageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(userData: UserData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflate the userresults layout for each item
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.userresults, parent, false)
        // Return the ViewHolder
        return UserViewHolder(itemView).apply {
            // Set the click listener for the entire item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(userList[position])
                }
            }
        }
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // Get the data for the current item
        val currentItem = userList[position]
        // Set the text for TextViews
        holder.userNameTextView.text = currentItem.firstname
        holder.userEmailTextView.text = currentItem.email
        holder.userImageView.setImageResource(R.drawable.baseline_image_24)

    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return userList.size
    }

    // Update the data set and refresh the RecyclerView
    fun updateData(newUserList: List<UserData>) {
        userList.clear()
        userList.addAll(newUserList)
        notifyDataSetChanged()
    }
}