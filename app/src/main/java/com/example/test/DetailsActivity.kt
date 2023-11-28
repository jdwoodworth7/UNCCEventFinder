package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.gms.maps.model.LatLng
import java.util.UUID

class DetailsActivity : AppCompatActivity() {
    private lateinit var userIdS: String
    private lateinit var userId: UUID
    private lateinit var eventTitle: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventAddress: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventImage: ImageView
    private lateinit var categories: LinearLayout

    private lateinit var interestButton: Button
    private lateinit var navigationButton: Button
    private lateinit var shareButton: Button
    private lateinit var menuButton: ImageView
    private lateinit var mapIcon: ImageView

    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        eventTitle = findViewById(R.id.detailTitle)
        interestButton = findViewById(R.id.interestButton)
        navigationButton = findViewById(R.id.navigationButton)
        menuButton = findViewById(R.id.menuButton)
        mapIcon = findViewById(R.id.mapIcon)
        eventDescription = findViewById(R.id.eventdescriptiontext)
        eventTitle = findViewById(R.id.detailTitle)
        eventLocation = findViewById(R.id.detailLocation)
        eventAddress = findViewById(R.id.detailAddress)
        eventTime = findViewById(R.id.eventTime)
        eventDate = findViewById(R.id.eventDate)
        eventImage = findViewById(R.id.eventImage)
        shareButton = findViewById(R.id.shareButton)

        val selectedEvent = intent.getParcelableExtra<EventData>("event")
        if (selectedEvent != null) {
            eventId = selectedEvent.id
            eventTitle.text = selectedEvent.title
            eventDescription.text = selectedEvent.description
            eventDate.text = selectedEvent.date
            eventTime.text = selectedEvent.time
            eventLocation.text = selectedEvent.buildingName
            eventAddress.text = selectedEvent.address
            eventImage.load(selectedEvent.imageUri)

            eventDescription.movementMethod = ScrollingMovementMethod()

            val categories = selectedEvent.categories

            for (category in categories) {
                val button = Button(this)
                button.text = category
                // Set any additional properties or click listeners for the button as needed

                // Add the button to your layout
                val linearLayout = findViewById<LinearLayout>(R.id.categoryButtonsLayout)
                linearLayout.addView(button)
            }

            val audiencesList = selectedEvent.audience

            for (audience in audiencesList) {
                val button = Button(this)
                button.text = audience
                // Set any additional properties or click listeners for the button as needed

                // Add the button to your layout
                val linearLayout = findViewById<LinearLayout>(R.id.audienceButtonsLayout)
                linearLayout.addView(button)
            }

            interestButton.setOnClickListener {
            }

            navigationButton.setOnClickListener {
                sendLocationNavigation(selectedEvent)
            }

            shareButton.setOnClickListener {
                shareEventDetails(selectedEvent)
            }
        }

            menuButton.setOnClickListener {
                // Open the menu activity when the menu button is clicked
                val intent = Intent(this@DetailsActivity, MenuActivity::class.java)
                startActivity(intent)
            }

            mapIcon.setOnClickListener {
                // Open the map activity when the map button is clicked
                val intent = Intent(this@DetailsActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }

    private fun shareEventDetails(event: EventData) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, event.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, buildEventDetailsText(event))

        startActivity(Intent.createChooser(shareIntent, "Share Event Details"))
    }

    private fun buildEventDetailsText(event: EventData): String {
        return """
            UNCC Event Finder
            ${event.title}
            ${event.description}
            ${event.date}
            ${event.time}
            ${event.buildingName}
            ${event.address}
            """.trimIndent()
    }

    private fun sendLocationNavigation(event: EventData) {
        fetchLatLngFromAddress(event) { lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(this)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }
}
