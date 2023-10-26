package com.example.test

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        //val cancelButton = findViewById<Button>(R.id.cancelButton)
        //val menuButton = findViewById<ImageView>(R.id.menuButton)
        //val mapIcon = findViewById<ImageView>(R.id.mapIcon)

        val descriptionCounter = findViewById<TextView>(R.id.descriptionCounter)
        val locationCounter = findViewById<TextView>(R.id.locationCounter)

        // Add a TextWatcher to update the description counter
        editDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the description counter
                val currentCount = s?.length ?: 0
                descriptionCounter.text = "$currentCount/200"
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing here
            }
        })

        // TextWatcher for locationEditText
        editBuildingName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the location counter
                val currentCount = s?.length ?: 0
                locationCounter.text = "$currentCount/80"
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing here
            }
        })

        continueButton.setOnClickListener {
            // Get the entered data
            val title = editTitle.text.toString()
            val description = editDescription.text.toString()
            val dateAndTime = editDateAndTime.text.toString()
            val buildingName = editBuildingName.text.toString()
            val address = editAddress.text.toString()

            if (title.isEmpty() || description.isEmpty() || dateAndTime.isEmpty() || buildingName.isEmpty() || address.isEmpty()) {
                // Display a message or toast indicating that all fields must be filled
                // For example:
                Toast.makeText(this@CreateEventActivity, "Please fill out all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
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

        //cancelButton.setOnClickListener {
        //    // If the user clicks the "Cancel" button, open the menu
        //    val intent = Intent(this@CreateEventActivity, MenuActivity::class.java)
        //    startActivity(intent)
        //}

        //menuButton.setOnClickListener {
        //    // Open the menu activity when the menu button is clicked
        //    val intent = Intent(this@CreateEventActivity, MenuActivity::class.java)
        //    startActivity(intent)
        //}

        //mapIcon.setOnClickListener {
        //    // Open the map activity when the map button is clicked
        //    val intent = Intent(this@CreateEventActivity, MapActivity::class.java)
        //    startActivity(intent)
        //}

    }
}