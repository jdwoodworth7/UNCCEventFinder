package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CreateEventDetailsActivity : AppCompatActivity() {

    // Firestore database 
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_details)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Retrieve data from the previous activity
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val buildingName = intent.getStringExtra("buildingName")
        val address = intent.getStringExtra("address")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Build a string to display checkbox details
        val checkBoxDetails = buildCheckBoxDetails()

        // Display the data in detailsEditText
        val detailsEditText = findViewById<EditText>(R.id.detailsEditText)
        detailsEditText.setText(
            "You have created an event!\n\n" +
                    "Title: $title\n" +
                    "Description: $description\n" +
                    "Date: $date" + "  " +  // Separate date
                    "Time: $time\n" +   // Separate time
                    "Building Name: $buildingName\n" +
                    "Address: $address\n" +
                    "Categories: $checkBoxDetails\n" +
                    "Image URI: $imageUrl"
        )

        // Setup "Exit" button click listener
        val backToCreateEventButton = findViewById<Button>(R.id.backToCreateEvent)
        backToCreateEventButton.setOnClickListener {
            // Retrieve checkbox states from the intent
            val checkBoxAcademic = intent.getBooleanExtra("checkboxAcademic", false)
            val checkBoxSocial = intent.getBooleanExtra("checkboxSocial", false)
            val checkBoxSports = intent.getBooleanExtra("checkboxSports", false)
            val checkBoxClubsOrg = intent.getBooleanExtra("checkboxClubsOrg", false)
            val checkBoxWorkshops = intent.getBooleanExtra("checkboxWorkshops", false)
            val checkBoxVolunteering = intent.getBooleanExtra("checkboxVolunteering", false)
            val checkBoxStudentsOnly = intent.getBooleanExtra("checkboxStudentsOnly", false)

            // Create a list of categories based on checkbox states
            val categories = mutableListOf<String>()
            if (checkBoxAcademic) categories.add("Academic")
            if (checkBoxSocial) categories.add("Social")
            if (checkBoxSports) categories.add("Sports")
            if (checkBoxClubsOrg) categories.add("Clubs/Organizations")
            if (checkBoxWorkshops) categories.add("Workshops/Seminars")
            if (checkBoxVolunteering) categories.add("Volunteering")
            if (checkBoxStudentsOnly) categories.add("Students Only")

            // Save event details to Firestore
            saveEventToFirestore(
                title ?: "",
                description ?: "",
                date ?: "",
                time ?: "",
                buildingName ?: "",
                address ?: "",
                categories ?: emptyList(),  // Provide an empty list if null
                imageUrl ?: ""
            )

            val intent = Intent(this@CreateEventDetailsActivity, MapsActivity::class.java)

            // Start the MapsActivity
            startActivity(intent)

            // Finish the current activity to remove it from the back stack
            finish()
        }
    }

    private fun buildCheckBoxDetails(): String {
        val checkBoxDetails = StringBuilder()

        // Retrieve checkbox states from the intent
        val checkBoxAcademic = intent.getBooleanExtra("checkboxAcademic", false)
        val checkBoxSocial = intent.getBooleanExtra("checkboxSocial", false)
        val checkBoxSports = intent.getBooleanExtra("checkboxSports", false)
        val checkBoxClubsOrg = intent.getBooleanExtra("checkboxClubsOrg", false)
        val checkBoxWorkshops = intent.getBooleanExtra("checkboxWorkshops", false)
        val checkBoxVolunteering = intent.getBooleanExtra("checkboxVolunteering", false)
        val checkBoxStudentsOnly = intent.getBooleanExtra("checkboxStudentsOnly", false)

        if (checkBoxAcademic) checkBoxDetails.append("Academic, ")
        if (checkBoxSocial) checkBoxDetails.append("Social, ")
        if (checkBoxSports) checkBoxDetails.append("Sports, ")
        if (checkBoxClubsOrg) checkBoxDetails.append("Clubs/Organizations, ")
        if (checkBoxWorkshops) checkBoxDetails.append("Workshops/Seminars, ")
        if (checkBoxVolunteering) checkBoxDetails.append("Volunteering, ")
        if (checkBoxStudentsOnly) checkBoxDetails.append("Students Only, ")

        // Remove the trailing comma and space
        return checkBoxDetails.toString().removeSuffix(", ")
    }

    private fun saveEventToFirestore(
        title: String,
        description: String,
        date: String?,
        time: String?,
        buildingName: String?,
        address: String?,
        categories: List<String>,
        imageUrl: String?
    ) {
        // Create a new event document in the "Events" collection
        val event = hashMapOf(
            "title" to title,
            "description" to description,
            "date" to date,
            "time" to time,
            "building_name" to buildingName,
            "address" to address,
            "image_url" to imageUrl,
            "category_academic" to categories.contains("Academic").toString(),
            "category_clubs" to categories.contains("Clubs/Organizations").toString(),
            "category_social" to categories.contains("Social").toString(),
            "category_sports" to categories.contains("Sports").toString(),
            "category_students_only" to categories.contains("Students Only").toString(),
            "category_volunteering" to categories.contains("Volunteering").toString(),
            "category_workshops" to categories.contains("Workshops/Seminars").toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        // Add the event to the "Events" collection
        db.collection("Events")
            .document()
            .set(event)
            .addOnSuccessListener {
                Log.d("Firestore", "Event document added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding event document", e)
            }
    }
}