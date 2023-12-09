package com.example.test
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.test.EventData

class EventAdapter2(context: Context, private val events: List<EventData>) : ArrayAdapter<EventData>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listView = convertView
        if (listView == null) {
            // Inflate the custom layout for each item
            listView = LayoutInflater.from(context).inflate(R.layout.upcomingactivites, parent, false)
        }

        // Get the current event
        val currentEvent = getItem(position)

        // Bind data to views
        val titleTextView: TextView = listView!!.findViewById(R.id.titletext)
        val detailsTextView: TextView = listView.findViewById(R.id.eventdetails)
        val imageView: ImageView = listView.findViewById(R.id.eventimage)

        // Check if the event is not null (for safety)
        currentEvent?.let {
            titleTextView.text = it.title
            detailsTextView.text = it.description
            // Load event image
            imageView.load(it.imageUri)
        }

        return listView
    }
}