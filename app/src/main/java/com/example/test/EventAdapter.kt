package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EventAdapter(private val eventList: MutableList<EventData>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder class to hold references to views in each item
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val buildingNameTextView: TextView = itemView.findViewById(R.id.buildingNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val sessionsTextView: TextView = itemView.findViewById(R.id.sessionsTextView)
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(eventData: EventData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Inflate the layout for each item
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        // Return the ViewHolder
        return EventViewHolder(itemView).apply {
            // Set the click listener for the entire item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(eventList[position])
                }
            }
        }
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Get the data for the current item
        val currentItem = eventList[position]
        // Set the text for TextViews
        holder.titleTextView.text = currentItem.title
        holder.buildingNameTextView.text = currentItem.buildingName
        holder.dateTextView.text = "${currentItem.date} ${currentItem.time}"
        holder.sessionsTextView.text = currentItem.eventSessionIds.toString()

        // Change from "userUploadedImageUri" to "userUploadedImageUrl"
        try {
            currentItem.imageUri?.let { imageUri ->
                // Use Picasso to load the image from the URL into the ImageView
                Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_24)
                    .into(holder.photoImageView)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Handle the exception, e.g., display a placeholder image
            holder.photoImageView.setImageResource(R.drawable.baseline_image_24)
        }

        // Fetch data from Firestore based on document IDs
        fetchEventDataFromFirestore(currentItem.eventSessionIds, holder.sessionsTextView)
    }

    private fun fetchEventDataFromFirestore(
        documentIds: List<String>,
        textView: TextView
    ) {
        val firestore = FirebaseFirestore.getInstance()

        // Example path, replace "EventSessions" with your actual Firestore collection name
        val collectionReference = firestore.collection("EventSessions")

        val data = mutableListOf<String>()

        val fetchTasks = documentIds.map { documentId ->
            collectionReference.document(documentId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val startDate = documentSnapshot.getString("startDate")
                        val startTime = documentSnapshot.getString("startTime")
                        val endDate = documentSnapshot.getString("endDate")
                        val endTime = documentSnapshot.getString("endTime")

                        // Add data to the list
                        data.add("Start: $startDate $startTime\nEnd: $endDate $endTime")

                        // Update the TextView with the fetched data after all documents are processed
                        textView.text = data.joinToString("\n\n")
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    exception.printStackTrace()
                }
        }

        // Use Tasks.whenAllSuccess to execute a callback when all tasks are successful
        Tasks.whenAllSuccess<Any?>(fetchTasks)
            .addOnSuccessListener { /* All tasks are successful */ }
            .addOnFailureListener { exception ->
                // Handle any errors during the tasks
                exception.printStackTrace()
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