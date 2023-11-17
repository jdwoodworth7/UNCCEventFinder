package com.example.test

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import java.util.UUID

class DetailsActivity : AppCompatActivity() {

    private lateinit var eventTitle: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventAddress: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventImage: ImageView

    private lateinit var interestButton: Button
    private lateinit var navigationButton: Button
    private lateinit var menuButton: ImageView
    private lateinit var mapIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventData = intent.getParcelableExtra<EventData>("event")


        interestButton = findViewById(R.id.interestButton)
        navigationButton = findViewById(R.id.navigationButton)
        menuButton = findViewById(R.id.menuButton)
        mapIcon = findViewById(R.id.mapIcon)

        //val eventId = UUID.fromString("your_event_id_here")
        //val eventDataList = getEventById(eventId)

        // Initialize EventDbAccess
        val eventDbAccess = EventDbAccess(this)
        // Get event data from the database
        val eventDataList = eventDbAccess.getEventDataFromDatabase()

        for (eventData in eventDataList) {
            eventDescription = findViewById(R.id.eventdescriptiontext)
            eventTitle = findViewById(R.id.detailTitle)
            eventLocation = findViewById(R.id.detailLocation)
            eventAddress = findViewById(R.id.detailAddress)
            eventTime = findViewById(R.id.eventTime)
            eventDate = findViewById(R.id.eventDate)
            eventImage = findViewById(R.id.eventImage)

            val categories = eventData.categories

            eventTitle.text = eventData.title
            eventDescription.text = eventData.description
            eventDate.text = eventData.date
            eventTime.text = eventData.time
            eventLocation.text = eventData.buildingName
            eventAddress.text = eventData.address
            //eventImage.image = eventData.imageUrl

            eventDescription.movementMethod = ScrollingMovementMethod()

            // Create buttons dynamically based on categories
            for (category in categories) {
                val button = Button(this)
                button.text = category
                // Set any additional properties or click listeners for the button as needed

                // Add the button to your layout
                val linearLayout = findViewById<LinearLayout>(R.id.categoryButtonsLayout)
                linearLayout.addView(button)
            }

            /*
        for (eventData in eventDataList) {
            val audience = eventData.audience

            for (audience in audiences) {
                val button = Button(this)
                button.text = audience
                // Set any additional properties or click listeners for the button as needed

                // Add the button to your layout
                val linearLayout = findViewById<LinearLayout>(R.id.audienceButtonsLayout)
                linearLayout.addView(button)
            }
        }
        */

            interestButton.setOnClickListener {
            }

            navigationButton.setOnClickListener {
                sendLocationNavigation(eventData)
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
    }
    private fun sendLocationNavigation(event: EventData) {
        fetchLatLngFromAddress(event) { lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(this)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }
}