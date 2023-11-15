package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {

    private lateinit var toggleAcademic: ToggleButton
    private lateinit var toggleSocial: ToggleButton
    private lateinit var toggleClubsOrg: ToggleButton
    private lateinit var toggleWorkshops: ToggleButton
    private lateinit var toggleVolunteering: ToggleButton
    private lateinit var toggleStudentsOnly: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Find ToggleButtons
        toggleAcademic = findViewById(R.id.toggleAcademic)
        toggleSocial = findViewById(R.id.toggleSocial)
        toggleClubsOrg = findViewById(R.id.toggleClubsOrg)
        toggleWorkshops = findViewById(R.id.toggleWorkshops)
        toggleVolunteering = findViewById(R.id.toggleVolunteering)
        toggleStudentsOnly = findViewById(R.id.toggleStudentsOnly)

        // Find Buttons
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set listeners for Buttons
        cancelButton.setOnClickListener {
            // Navigate to SearchActivity
            navigateToSearchActivity()
        }

        submitButton.setOnClickListener {
            // Navigate to SearchActivity and pass selected filters
            navigateToSearchActivityWithFilters()
        }

        // Find Menu Button
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Navigate to MenuActivity
            navigateToMenuActivity()
        }

        // Find Map Button
        val mapButton = findViewById<ImageView>(R.id.mapIcon)
        mapButton.setOnClickListener {
            // Navigate to MapsActivity
            navigateToMapsActivity()
        }
    }

    private fun navigateToSearchActivity() {
        val intent = Intent(this@FilterActivity, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSearchActivityWithFilters() {
        val intent = Intent(this@FilterActivity, SearchActivity::class.java)
        // Pass selected filters to SearchActivity
        intent.putExtra("academic", toggleAcademic.isChecked)
        intent.putExtra("social", toggleSocial.isChecked)
        intent.putExtra("clubsOrg", toggleClubsOrg.isChecked)
        intent.putExtra("workshops", toggleWorkshops.isChecked)
        intent.putExtra("volunteering", toggleVolunteering.isChecked)
        intent.putExtra("studentsOnly", toggleStudentsOnly.isChecked)
        startActivity(intent)
    }

    private fun navigateToMenuActivity() {
        val intent = Intent(this@FilterActivity, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMapsActivity() {
        val intent = Intent(this@FilterActivity, MapsActivity::class.java)
        startActivity(intent)
    }
}