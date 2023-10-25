package com.example.test

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_details)

        // Retrieve data passed from the previous activity
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val dateAndTime = intent.getStringExtra("dateAndTime")
        val buildingName = intent.getStringExtra("buildingName")
        val address = intent.getStringExtra("address")
        val categories = intent.getStringArrayExtra("categories")

        // Display the data in TextViews (replace R.id.textViewTitle, etc., with your actual IDs)
        val checkboxAcademic = findViewById<CheckBox>(R.id.checkboxAcademic)
        val checkboxSocial = findViewById<CheckBox>(R.id.checkboxSocial)
        val checkboxSports = findViewById<CheckBox>(R.id.checkboxSports)
        val checkboxClubsOrg = findViewById<CheckBox>(R.id.checkboxClubsOrg)
        val checkboxWorkshops = findViewById<CheckBox>(R.id.checkboxWorkshops)
        val checkboxVolunteering = findViewById<CheckBox>(R.id.checkboxVolunteering)
        val checkboxStudentsOnly = findViewById<CheckBox>(R.id.checkboxStudentsOnly)

        textViewTitle.text = "Title: $title"
        textViewDescription.text = "Description: $description"
        textViewDateAndTime.text = "Date and Time: $dateAndTime"
        textViewBuildingName.text = "Building Name: $buildingName"
        textViewAddress.text = "Address: $address"

        // Display categories if available
        if (categories != null && categories.isNotEmpty()) {
            val categoriesString = categories.joinToString(", ")
            textViewCategories.text = "Categories: $categoriesString"
        } else {
            textViewCategories.text = "Categories: Not specified"
        }
    }
}