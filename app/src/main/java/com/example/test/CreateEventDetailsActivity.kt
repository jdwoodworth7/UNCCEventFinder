package com.example.test

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.ImageView
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
            // Retrieve checkbox states from the intent
            val checkBoxAcademic = intent.getBooleanExtra("checkboxAcademic", false)
            val checkBoxSocial = intent.getBooleanExtra("checkboxSocial", false)
            val checkBoxSports = intent.getBooleanExtra("checkboxSports", false)
            val checkBoxClubsOrg = intent.getBooleanExtra("checkboxClubsOrg", false)
            val checkBoxWorkshops = intent.getBooleanExtra("checkboxWorkshops", false)
            val checkBoxVolunteering = intent.getBooleanExtra("checkboxVolunteering", false)
            val checkBoxStudentsOnly = intent.getBooleanExtra("checkboxStudentsOnly", false)

            // Create a list of categories based on checkbox states
            val categories = mutableListOf<String>()
            if (checkBoxAcademic) categories.add("Academic")
            if (checkBoxSocial) categories.add("Social")
            if (checkBoxSports) categories.add("Sports")
            if (checkBoxClubsOrg) categories.add("Clubs/Organizations")
            if (checkBoxWorkshops) categories.add("Workshops/Seminars")
            if (checkBoxVolunteering) categories.add("Volunteering")
            if (checkBoxStudentsOnly) categories.add("Students Only")

            // Save event details to the database
            saveEventToDatabase(
                title ?: "",          // Provide a default empty string if null
                description ?: "",
                dateAndTime,
                buildingName,
                address,
                categories
            )

<<<<<<< Updated upstream
<<<<<<< Updated upstream
            // Create an Intent to start MainActivity
=======
            // Create an Intent to start CreateEventActivity
>>>>>>> Stashed changes
=======
            // Create an Intent to start CreateEventActivity
>>>>>>> Stashed changes
            val intent = Intent(this@CreateEventDetailsActivity, CreateEventActivity::class.java)

            // Start the CreateEventActivity
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

    private fun saveEventToDatabase(
        title: String,
        description: String,
        dateAndTime: String?,
        buildingName: String?,
        address: String?,
        categories: List<String>
    ) {
        val dbHelper = EventDbHelper(this)
        val db = dbHelper.writableDatabase

        // Insert event details
        val values = ContentValues().apply {
            put(EventContract.EventEntry.COLUMN_TITLE, title)
            put(EventContract.EventEntry.COLUMN_DESCRIPTION, description)
            dateAndTime?.let { put(EventContract.EventEntry.COLUMN_DATETIME, it) }
            buildingName?.let { put(EventContract.EventEntry.COLUMN_BUILDING_NAME, it) }
            address?.let { put(EventContract.EventEntry.COLUMN_ADDRESS, it) }

            // Set category columns
            put(EventContract.EventEntry.COLUMN_CATEGORY_ACADEMIC, if ("Academic" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_SOCIAL, if ("Social" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_SPORTS, if ("Sports" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_CLUBS_ORG, if ("Clubs/Organizations" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_WORKSHOPS, if ("Workshops/Seminars" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_VOLUNTEERING, if ("Volunteering" in categories) 1 else 0)
            put(EventContract.EventEntry.COLUMN_CATEGORY_STUDENTS_ONLY, if ("Students Only" in categories) 1 else 0)
        }

        val newRowId = db.insert(EventContract.EventEntry.TABLE_NAME, null, values)

        if (newRowId != -1L) {
            // The insertion was successful, and newRowId contains the ID of the new row
            Log.d("Database", "Event row inserted successfully with ID: $newRowId")

            // You can log or handle the success of category insertion here
        } else {
            Log.e("Database", "Error inserting event row")
            // Handle the case where event insertion failed
        }
    }
}