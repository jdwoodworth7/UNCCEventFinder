package com.example.test

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CreateEventCategoriesActivity : AppCompatActivity() {
    // Declare displayText as a class-level property
    private lateinit var displayText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_categories)
        // Initialize displayText
        displayText = findViewById(R.id.displayText)

        // Retrieve data passed from the previous activity
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val dateAndTime = intent.getStringExtra("dateAndTime")
        val buildingName = intent.getStringExtra("buildingName")
        val address = intent.getStringExtra("address")

        // Display the data in a TextView (you can replace this with your actual UI elements)
        val displayText: TextView = findViewById(R.id.displayText)
        displayText.text = "Title: $title\nDescription: $description\nDate and Time: $dateAndTime\nBuilding Name: $buildingName\nAddress: $address"

        // Handle checkboxes here (replace R.id.checkboxAcademic, R.id.checkboxSocial, etc. with your actual IDs)
        val checkboxAcademic = findViewById<CheckBox>(R.id.checkboxAcademic)
        val checkboxSocial = findViewById<CheckBox>(R.id.checkboxSocial)
        val checkboxSports = findViewById<CheckBox>(R.id.checkboxSports)
        val checkboxClubsOrg = findViewById<CheckBox>(R.id.checkboxClubsOrg)
        val checkboxWorkshops = findViewById<CheckBox>(R.id.checkboxWorkshops)
        val checkboxVolunteering = findViewById<CheckBox>(R.id.checkboxVolunteering)
        val checkboxStudentsOnly = findViewById<CheckBox>(R.id.checkboxStudentsOnly)

        // Set up listeners for checkboxes
        checkboxAcademic.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Academic", isChecked)
        }

        checkboxSocial.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Social", isChecked)
        }

        checkboxSports.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Sports", isChecked)
        }

        checkboxClubsOrg.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Clubs/Organizations", isChecked)
        }

        checkboxWorkshops.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Workshops/Seminars", isChecked)
        }

        checkboxVolunteering.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Volunteering", isChecked)
        }

        checkboxStudentsOnly.setOnCheckedChangeListener { _, isChecked ->
            updateDisplayText("Students Only", isChecked)
        }

    }

    private fun updateDisplayText(category: String, isChecked: Boolean) {
        val displayText: TextView = findViewById(R.id.displayText)

        if (isChecked) {
            // Category is selected, append to displayText
            displayText.append("\nCategory: $category")
        } else {
            // Category is unselected, update displayText to remove the category
            val currentText = displayText.text.toString()
            val updatedText = currentText.replace("\nCategory: $category", "")
            displayText.text = updatedText
        }
    }
}