package com.example.test // Change this package name to match your app's package

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Find the "Create an Event" TextView
        val createEventTextView: TextView = findViewById(R.id.createEvent)

        // Set an onClickListener for "Create an Event"
        createEventTextView.setOnClickListener {

            // Launch CreateEventActivity
            val intent = Intent(this@MenuActivity, CreateEventActivity::class.java)
            startActivity(intent)
        }

    }
}