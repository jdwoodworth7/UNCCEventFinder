package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalTime


class CalendarViewActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var listView: ListView

    private lateinit var eventTitle: TextView
    private lateinit var eventTime: TextView

    private lateinit var time: LocalTime


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)
        initWidgets()
        //Giving it the current date and time of local pc
        selectedDate = LocalDate.now()
        setMonthView()
        time = LocalTime.now()
        setListViewAdapter()

        // Find the menu button and set a click listener
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@CalendarViewActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the map icon and set a click listener
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        mapIcon.setOnClickListener {
            // Open the MapActivity when the map icon is clicked
            val intent = Intent(this@CalendarViewActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()
        setListViewAdapter()
    }

    //Sets the adapter and finds which events are on that day via eventsForDate
    private fun setListViewAdapter() {
        val dailyEvents: ArrayList<EventData> = eventsForDate(selectedDate)
        val calendarListAdapter = CalendarListAdapter(this, dailyEvents)
        listView.adapter = calendarListAdapter
    }


    //Queries the database to see which events are on which day and adds them to a temp array if dates match
    fun eventsForDate(date: LocalDate): ArrayList<EventData> {
        //Gets database access
        val eventDbAccess = EventDbAccess(this)
        val eventList = eventDbAccess.getEventDataFromDatabase()
        val events = ArrayList<EventData>()
        //Checking if dates match to put in the new array
        for (event in eventList) {
            if (event.date == date)
                events.add(event)
        }
        return events
    }

    //Find the recycler view and text view on startup and make them variables
    private fun initWidgets() {
        listView = findViewById(R.id.calendarListView)
        monthYearText = findViewById(R.id.monthYearTV)
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
    }

    //Actually create the view based on the month out of 7 columns
    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    //Go back a month button
    fun previousMonthAction(view: View) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    //Go forward a month button
    fun nextMonthAction(view: View) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    //Temp click listener
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            selectedDate = date
            setListViewAdapter()
            setMonthView()
        }
    }


}