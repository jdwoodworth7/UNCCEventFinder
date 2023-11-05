package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Find ToggleButtons
        val toggleAcademic = findViewById<ToggleButton>(R.id.toggleAcademic)
        val toggleSocial = findViewById<ToggleButton>(R.id.toggleSocial)
        val toggleClubsOrg = findViewById<ToggleButton>(R.id.toggleClubsOrg)
        val toggleWorkshops = findViewById<ToggleButton>(R.id.toggleWorkshops)
        val toggleVolunteering = findViewById<ToggleButton>(R.id.toggleVolunteering)
        val toggleStudentsOnly = findViewById<ToggleButton>(R.id.toggleStudentsOnly)

        // Find Buttons
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set listeners for ToggleButtons if needed

        // Set listeners for Buttons
        cancelButton.setOnClickListener {
            // Navigate to SearchActivity
            val intent = Intent(this@FilterActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            // Navigate to SearchActivity
            val intent = Intent(this@FilterActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Find Menu Button
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Navigate to MenuActivity
            val intent = Intent(this@FilterActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find Map Button
        val mapButton = findViewById<ImageView>(R.id.mapIcon)
        mapButton.setOnClickListener {
            // Navigate to MapsActivity
            val intent = Intent(this@FilterActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}