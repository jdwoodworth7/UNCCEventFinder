package com.example.test

import android.app.Activity
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.time.LocalDate
import coil.load



class CalendarListAdapter (private val context : Activity, private val arrayList : ArrayList<EventData> ) : ArrayAdapter<EventData>(context, R.layout.activity_calendar_list_view, arrayList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Sets up the listview
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_calendar_list_view, null)

        //Used for event image
        val imageUrl: String? = arrayList[position].userUploadedImageUrl

        //Finds the listview parts
        val icon : ImageView = view.findViewById(R.id.calendarListIcon)
        val title : TextView = view.findViewById(R.id.calendarListTitle)
        val time : TextView = view.findViewById(R.id.calendarListTime)
        val navButton : Button = view.findViewById(R.id.buttonNavigate)
        val removeButton : Button = view.findViewById(R.id.buttonRemove)

        //Uses coil to load the image url from a file pathway string
        icon.load(imageUrl)
        icon.visibility = View.VISIBLE
        title.text = arrayList[position].title



        return view
    }
}