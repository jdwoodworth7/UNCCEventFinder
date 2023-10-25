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

        val editTitle: EditText = findViewById(R.id.titleEditText)
        val editDescription: EditText = findViewById(R.id.descriptionEditText)
        val editDateAndTime: EditText = findViewById(R.id.dateAndTimeEditText)
        val editBuildingName: EditText = findViewById(R.id.locationEditText)
        val editAddress: EditText = findViewById(R.id.addressEditText)
        val continueButton: Button = findViewById(R.id.continueButton)

        continueButton.setOnClickListener {
            // Get the entered data
            val title = editTitle.text.toString()
            val description = editDescription.text.toString()
            val dateAndTime = editDateAndTime.text.toString()
            val buildingName = editBuildingName.text.toString()
            val address = editAddress.text.toString()

            // Create an Intent to start the next activity
            val intent = Intent(this, CreateEventCategoriesActivity::class.java)

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