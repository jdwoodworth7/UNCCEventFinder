package com.example.test

import android.content.ClipDescription
import android.content.Intent
import android.location.Address
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.time.LocalTime


class DetailsActivity : AppCompatActivity() {

    private lateinit var interestButton: Button
    private lateinit var navigationButton: Button

    private lateinit var monthYearText: TextView
    private lateinit var eventTitle: TextView
    private lateinit var time: LocalTime
    private lateinit var address : Address
    private lateinit var description: ClipDescription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        interestButton = findViewById(R.id.interestButton)
        navigationButton = findViewById(R.id.navigationButton)

        interestButton.setOnClickListener {
        }

        navigationButton.setOnClickListener {
        }

    }
}