package com.example.test

import android.app.Activity
import android.content.Intent
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
import com.example.test.EventDbAccess



class CalendarListAdapter (private val context : Activity, private val arrayList : ArrayList<EventData> ) : ArrayAdapter<EventData>(context, R.layout.activity_calendar_list_view, arrayList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Sets up the listview
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_calendar_list_view, null)

        //Used for event image
        val imageUrl: String? = arrayList[position].imageUri

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
        time.text = arrayList[position].time

        //onClickListener for NavigationButton
        navButton.setOnClickListener{
            handleNavigationButtonClick(position)
        }

        removeButton.setOnClickListener{
            handleRemovalButtonClick(position)
        }


        return view
    }

    private fun handleNavigationButtonClick(position: Int){
        //focuses on the selected event among events in the list (same date)
        val eventData = arrayList[position]

        //queries latitude and longitude and sends value to NavigationAppIntegration
        fetchLatLngFromAddress(eventData) {lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(context)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }

    private fun handleRemovalButtonClick(position: Int){
        //focuses on the selected event among events in the list (same date)
        val eventData = arrayList[position]
        val eventTitle:String = eventData.title

        //removes the event
        EventDbAccess.removeEventFromDatabase(context, eventTitle)

        val intent = Intent(context, CalendarViewActivity::class.java)
        context.startActivity(intent)
    }
}