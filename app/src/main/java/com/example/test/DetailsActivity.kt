package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.gms.maps.model.LatLng
import java.util.UUID
import android.content.Context
import android.text.Layout
import android.view.Gravity
import android.widget.GridLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.net.URL

class DetailsActivity : AppCompatActivity() {
    private lateinit var userIdS: String
    private lateinit var userId: UUID
    private lateinit var eventTitle: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventStartDate: TextView
    private lateinit var eventEndDate: TextView
    private lateinit var eventAddress: TextView
    private lateinit var eventLocation: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventImage: ImageView
    private lateinit var categories: LinearLayout

    private lateinit var interestButton: Button
    private lateinit var navigationButton: Button
    private lateinit var shareButton: Button
    private lateinit var reportButton: AppCompatImageButton
    private lateinit var menuButton: ImageView
    private lateinit var mapIcon: ImageView

    private lateinit var eventId: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var dateAdapter: DateAdapter

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
        //eventTime = findViewById(R.id.eventTime)
        eventStartDate = findViewById(R.id.eventStartDate)
        eventEndDate = findViewById(R.id.eventEndDate)
        eventImage = findViewById(R.id.eventImage)
        shareButton = findViewById(R.id.shareButton)
        reportButton = findViewById(R.id.reportButton)

        //gets fetched event data from the previous activity
        val selectedEvent = intent.getParcelableExtra<EventData>("event")
        if (selectedEvent != null) {
            eventId = selectedEvent.id
            eventTitle.text = selectedEvent.title
            eventDescription.text = selectedEvent.description
            eventStartDate.text = selectedEvent.date
            eventTime.text = selectedEvent.time
            eventLocation.text = selectedEvent.buildingName
            eventAddress.text = selectedEvent.address
            eventImage.load(selectedEvent.imageUri)

            val categories = selectedEvent.categories

            for (category in categories) {
                val button = Button(this)
                button.text = category
                // Set any additional properties or click listeners for the button as needed
                val params = GridLayout.LayoutParams()
                params.width = GridLayout.LayoutParams.WRAP_CONTENT
                params.height = GridLayout.LayoutParams.WRAP_CONTENT
                params.setMargins(0, 0, 0, 0) // Adjust margins as needed
                button.layoutParams = params

                // Add the button to your layout
                val gridLayout: LinearLayout = findViewById(R.id.categoryButtonsLayout)
                gridLayout.addView(button)
            }

            val audiencesList = selectedEvent.audience

            for (audience in audiencesList) {
                val button = Button(this)
                button.text = audience
                val params = GridLayout.LayoutParams()
                params.width = GridLayout.LayoutParams.WRAP_CONTENT
                params.height = GridLayout.LayoutParams.WRAP_CONTENT
                params.setMargins(0, 0, 0, 0) // Adjust margins as needed
                button.layoutParams = params

                // Set any additional properties or click listeners for the button as needed
                // Add the button to your layout
                val gridLayout: LinearLayout = findViewById(R.id.audienceButtonsLayout)
                gridLayout.addView(button)
            }

            interestButton.setOnClickListener {
            }

            navigationButton.setOnClickListener {
                sendLocationNavigation(selectedEvent)
            }

            shareButton.setOnClickListener {
                shareEventDetails(selectedEvent, this@DetailsActivity)
            }
            reportButton.setOnClickListener{
                reportEvent(selectedEvent)
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

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Replace "your_collection" with your Firestore collection name
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("your_collection")

        // Retrieve dates from Firestore
        collectionReference.get()
            .addOnSuccessListener { documents ->
                val dateList = mutableListOf<DateModel>()
                for (document in documents) {
                    val date = document.getString("date") ?: ""
                    dateList.add(DateModel(date))
                }

                dateAdapter = DateAdapter(dateList)
                recyclerView.adapter = dateAdapter
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }

    }

    private fun sendLocationNavigation(event: EventData) {
        fetchLatLngFromAddress(event) { lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(this)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }

    private fun shareEventDetails(event: EventData, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Download the image to a file if the imageUri is not null
                val imageFile = event.imageUri?.let { downloadImage(it, context) }

                // Create a tweet text
                val tweetText = "📅 Exciting Event Alert! 🎉 Check out ${event.title} on UNCC Event Finder 📱 #UNCCEventFinder\n" +
                        "\n" +
                        "📍 ${event.buildingName}\n" +
                        "📆 ${event.date}\n" +
                        "🕒 ${event.time}\n" +
                        "\n" +
                        "📝 ${event.description}\n" +
                        "\n" +
                        "#Event #UNCC #UNCCEventFinder"

                // Create an ACTION_SEND intent
                val shareIntent = Intent(Intent.ACTION_SEND)

                // Set the type of content to "text/plain"
                shareIntent.type = "text/plain"

                // Attach the tweet text
                shareIntent.putExtra(Intent.EXTRA_TEXT, tweetText)

                // Attach the image URI to the intent (if available)
                imageFile?.let {
                    // Use a FileProvider to get the content URI for the file
                    val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
                }

                // Start the activity with the chooser dialog
                context.startActivity(Intent.createChooser(shareIntent, "Share event details"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun downloadImage(url: String, context: Context): File {
        val connection = URL(url).openConnection()
        connection.connect()

        val inputStream: InputStream = connection.getInputStream()
        val file = File(context.cacheDir, UUID.randomUUID().toString() + ".jpg")
        file.outputStream().use { fileOutputStream ->
            inputStream.copyTo(fileOutputStream)
        }
        return file
    }

    //starts the report event activity with intent
    private fun reportEvent(selectedEvent: EventData) {
            val intent = Intent(this@DetailsActivity, ReportCreateActivity::class.java)

            //put the Event object (parcelized) as an extra in the intent
            intent.putExtra("selectedEvent", selectedEvent)

            // Start the next activity
            startActivity(intent)
    }
}