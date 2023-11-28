package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CreateEventSessionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_sessions)

        val submitButton = findViewById<Button>(R.id.submitButton)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)

        submitButton.setOnClickListener {

        }

        continueButton.setOnClickListener {

            val intent = Intent(this@CreateEventSessionsActivity, CreateEventCategoriesActivity::class.java)


            // Start the next activity
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            // If the user clicks the "Cancel" button, finish the activity
            finish()
        }

        menuButton.setOnClickListener {
            // Open the menu activity when the menu button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        mapIcon.setOnClickListener {
            // Open the map activity when the map button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}