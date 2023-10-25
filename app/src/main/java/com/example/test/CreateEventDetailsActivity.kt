package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateEventDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_details)

        // Retrieve data from the previous activity
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val dateAndTime = intent.getStringExtra("dateAndTime")
        val buildingName = intent.getStringExtra("buildingName")
        val address = intent.getStringExtra("address")

        // Build a string to display checkbox details
        val checkBoxDetails = buildCheckBoxDetails()

        // Display the data in detailsEditText
        val detailsEditText = findViewById<EditText>(R.id.detailsEditText)
        detailsEditText.setText(
            "You have created an event!\n\n" +
                    "Title: $title\n" +
                    "Description: $description\n" +
                    "Date and Time: $dateAndTime\n" +
                    "Building Name: $buildingName\n" +
                    "Address: $address\n\n" +
                    "Categories: $checkBoxDetails"
        )

        // Setup "Exit" button click listener
        val backToCreateEventButton = findViewById<Button>(R.id.backToCreateEvent)
        backToCreateEventButton.setOnClickListener {
            // Create an Intent to start MainActivity
            val intent = Intent(this@CreateEventDetailsActivity, MainActivity::class.java)

            // Start the MainActivity
            startActivity(intent)

            // Finish the current activity to remove it from the back stack
            finish()
        }
    }

    private fun buildCheckBoxDetails(): String {
        val checkBoxDetails = StringBuilder()

        // Retrieve checkbox states from the intent
        val checkBoxAcademic = intent.getBooleanExtra("checkboxAcademic", false)
        val checkBoxSocial = intent.getBooleanExtra("checkboxSocial", false)
        val checkBoxSports = intent.getBooleanExtra("checkboxSports", false)
        val checkBoxClubsOrg = intent.getBooleanExtra("checkboxClubsOrg", false)
        val checkBoxWorkshops = intent.getBooleanExtra("checkboxWorkshops", false)
        val checkBoxVolunteering = intent.getBooleanExtra("checkboxVolunteering", false)
        val checkBoxStudentsOnly = intent.getBooleanExtra("checkboxStudentsOnly", false)

        if (checkBoxAcademic) checkBoxDetails.append("Academic, ")
        if (checkBoxSocial) checkBoxDetails.append("Social, ")
        if (checkBoxSports) checkBoxDetails.append("Sports, ")
        if (checkBoxClubsOrg) checkBoxDetails.append("Clubs/Organizations, ")
        if (checkBoxWorkshops) checkBoxDetails.append("Workshops/Seminars, ")
        if (checkBoxVolunteering) checkBoxDetails.append("Volunteering, ")
        if (checkBoxStudentsOnly) checkBoxDetails.append("Students Only, ")

        // Remove the trailing comma and space
        return checkBoxDetails.toString().removeSuffix(", ")
    }
}