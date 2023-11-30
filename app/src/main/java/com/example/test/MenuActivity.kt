package com.example.test // Change this package name to match your app's package

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Find the "Create an Event" TextView
        val createEventTextView: TextView = findViewById(R.id.createEvent)

        // Set an onClickListener for "Create an Event"
        createEventTextView.setOnClickListener {

            // Launch CreateEventActivity
            val intent = Intent(this@MenuActivity, CreateEventActivity::class.java)
            startActivity(intent)
        }

        // Find the "Search Event" TextView
        val searchEventTextView: TextView = findViewById(R.id.searchEvent)

        // Set an onClickListener for "Search Event"
        searchEventTextView.setOnClickListener {
            // Launch SearchActivity
            val intent = Intent(this@MenuActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Find the "My Schedules" TextView
        val calendarViewTextView: TextView = findViewById(R.id.mySchedules)

        // Set an onClickListener for "My Schedules"
        calendarViewTextView.setOnClickListener {

            // Launch CalendarViewActivity
            val intent = Intent(this@MenuActivity, CalendarViewActivity::class.java)
            startActivity(intent)
        }
    }
}