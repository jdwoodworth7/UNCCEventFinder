package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val eventList: MutableList<EventData>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val buildingNameTextView: TextView = itemView.findViewById(R.id.buildingNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.titleTextView.text = currentItem.title
        holder.buildingNameTextView.text = currentItem.buildingName
        holder.dateTextView.text = currentItem.dateAndTime
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun updateData(newEventList: List<EventData>) {
        eventList.clear()
        eventList.addAll(newEventList)
        notifyDataSetChanged()
    }
}