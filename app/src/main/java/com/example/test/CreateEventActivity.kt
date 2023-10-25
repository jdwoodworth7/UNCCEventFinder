package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Assuming you have the relevant EditText and Button IDs in your activity_main.xml
        val editTitle = findViewById<EditText>(R.id.titleEditText)
        val editDescription = findViewById<EditText>(R.id.descriptionEditText)
        val editDateAndTime = findViewById<EditText>(R.id.dateAndTimeEditText)
        val editBuildingName = findViewById<EditText>(R.id.locationEditText)
        val editAddress = findViewById<EditText>(R.id.addressEditText)
        val continueButton = findViewById<Button>(R.id.continueButton)

        continueButton.setOnClickListener {
            // Get the entered data
            val title = editTitle.text.toString()
            val description = editDescription.text.toString()
            val dateAndTime = editDateAndTime.text.toString()
            val buildingName = editBuildingName.text.toString()
            val address = editAddress.text.toString()

            // Create an Intent to start the next activity
            val intent = Intent(this@CreateEventActivity, CreateEventCategoriesActivity::class.java)

            // Pass the data to the next activity
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("dateAndTime", dateAndTime)
            intent.putExtra("buildingName", buildingName)
            intent.putExtra("address", address)

            // Start the next activity
            startActivity(intent)
        }
    }
}