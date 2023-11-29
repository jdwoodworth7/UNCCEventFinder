package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateEventSessionsActivity : AppCompatActivity() {

    private val sessionsList = mutableListOf<List<String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_sessions)

        val submitButton = findViewById<Button>(R.id.submitButton)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        val startDateButton = findViewById<Button>(R.id.startDateButton)
        val startTimeButton = findViewById<Button>(R.id.startTimeButton)
        val endDateButton = findViewById<Button>(R.id.endDateButton)
        val endTimeButton = findViewById<Button>(R.id.endTimeButton)
        val currentSessionsTextView = findViewById<TextView>(R.id.currentSessions)

        submitButton.setOnClickListener {
            // Get values from EditText fields
            val startDate = startDateButton.text.toString()
            val startTime = startTimeButton.text.toString()
            val endDate = endDateButton.text.toString()
            val endTime = endTimeButton.text.toString()

            // Create a session array and add it to the sessionsList
            val session = listOf(startDate, startTime, endDate, endTime)
            sessionsList.add(session)

            // Update the currentSessions TextView with the formatted array
            updateCurrentSessionsTextView(currentSessionsTextView)
        }

        continueButton.setOnClickListener {
            // Retrieve data from the previous activity
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")
            val buildingName = intent.getStringExtra("buildingName")
            val address = intent.getStringExtra("address")
            val imageUri = intent.getStringExtra("imageUri")

            val intent = Intent(this@CreateEventSessionsActivity, CreateEventCategoriesActivity::class.java)

            // Pass the data to the next activity
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            intent.putExtra("buildingName", buildingName)
            intent.putExtra("address", address)
            intent.putExtra("imageUri", imageUri)

            // Pass the sessionsList to the next activity
            intent.putExtra("sessionsList", sessionsList.map { it.toTypedArray() }.toTypedArray())

            // Start the next activity
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            // If the user clicks the "Cancel" button, finish the activity
            finish()
        }

        menuButton.setOnClickListener {
            // Open the menu activity when the menu button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        mapIcon.setOnClickListener {
            // Open the map activity when the map button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateCurrentSessionsTextView(textView: TextView) {
        // Format the sessionsList and update the TextView
        val formattedSessions = sessionsList.joinToString("\n") { session ->
            session.joinToString(", ")
        }
        textView.text = formattedSessions
    }

}