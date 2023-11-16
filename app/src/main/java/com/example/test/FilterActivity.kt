package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {

    private lateinit var toggleAcademic: ToggleButton
    private lateinit var toggleSocial: ToggleButton
    private lateinit var toggleClubsOrg: ToggleButton
    private lateinit var toggleWorkshops: ToggleButton
    private lateinit var toggleVolunteering: ToggleButton
    private lateinit var toggleStudentsOnly: ToggleButton
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Initialize ToggleButtons and Button
        toggleAcademic = findViewById(R.id.toggleAcademic)
        toggleSocial = findViewById(R.id.toggleSocial)
        toggleClubsOrg = findViewById(R.id.toggleClubsOrg)
        toggleWorkshops = findViewById(R.id.toggleWorkshops)
        toggleVolunteering = findViewById(R.id.toggleVolunteering)
        toggleStudentsOnly = findViewById(R.id.toggleStudentsOnly)
        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Set click listener for the Submit button
        submitButton.setOnClickListener {
            // Collect the selected filter data
            val filterData = FilterData(
                academic = toggleAcademic.isChecked,
                social = toggleSocial.isChecked,
                clubsOrg = toggleClubsOrg.isChecked,
                workshops = toggleWorkshops.isChecked,
                volunteering = toggleVolunteering.isChecked,
                studentsOnly = toggleStudentsOnly.isChecked
            )
            // Log filter data
            Log.d("FilterActivity", "Filter Data: $filterData")
            // Create an Intent to pass back the filter data
            val resultIntent = Intent()
            resultIntent.putExtra("filterData", filterData)

            // Set the result and finish the activity
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        // Set click listener for the Cancel button
        cancelButton.setOnClickListener {
            // Finish the activity without sending any data
            setResult(RESULT_CANCELED)
            finish()
        }

        // Find the menu button and set a click listener
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@FilterActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the map icon and set a click listener
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        mapIcon.setOnClickListener {
            // Open the MapActivity when the map icon is clicked
            val intent = Intent(this@FilterActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}