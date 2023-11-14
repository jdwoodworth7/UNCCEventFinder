package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class EventAdapter(private val eventList: MutableList<EventData>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder class to hold references to views in each item
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val buildingNameTextView: TextView = itemView.findViewById(R.id.buildingNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Inflate the layout for each item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        // Return the ViewHolder
        return EventViewHolder(itemView)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Get the data for the current item
        val currentItem = eventList[position]
        // Set the text for TextViews
        holder.titleTextView.text = currentItem.title
        holder.buildingNameTextView.text = currentItem.buildingName
        holder.dateTextView.text = currentItem.date

        // Change from "userUploadedImageUri" to "userUploadedImageUrl"
        try {
            currentItem.userUploadedImageUrl?.let { imageUrl ->
                // Use Coil to load the image from the URL into the ImageView
                holder.photoImageView.load(imageUrl) {
                    // You can customize image loading options if needed
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Handle the exception, e.g., display a placeholder image
            holder.photoImageView.setImageResource(R.drawable.baseline_image_24)
        }
    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return eventList.size
    }

    // Update the data set and refresh the RecyclerView
    fun updateData(newEventList: List<EventData>) {
        eventList.clear()
        eventList.addAll(newEventList)
        notifyDataSetChanged()
    }
}