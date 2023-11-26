package com.example.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreateEventDetailsActivity : AppCompatActivity() {

    // Firestore database
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

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
        val imageUri = intent.getStringExtra("imageUri")

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
                    "Image URI: $imageUri"
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

            // Save event details to Firestore and upload image to Storage
            saveEventToFirestore(
                title ?: "",
                description ?: "",
                date ?: "",
                time ?: "",
                buildingName ?: "",
                address ?: "",
                categories ?: emptyList(),  // Provide an empty list if null
                imageUri ?: ""
            )
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
        imageUri: String?
    ) {
        Log.d("Firestore", "Saving event to Firestore")

        // Upload image to Firebase Storage
        if (!imageUri.isNullOrBlank() && !imageUri.startsWith("gs://")) {
            Log.d("Firestore", "Image URI is a local content URI, proceeding with Firestore upload")

            // Get the file name from the imageUri
            val fileName = "event_images/${System.currentTimeMillis()}_${Uri.parse(imageUri).lastPathSegment}"

            // Get a reference to the storage location
            val imageRef = storageRef.child(fileName)

            // Upload the file
            imageRef.putFile(Uri.parse(imageUri))
                .addOnSuccessListener { taskSnapshot ->
                    Log.d("FirebaseStorage", "Image uploaded successfully")

                    // Get the download URL for the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Continue with saving the event data to Firestore
                        saveEventDataToFirestore(title, description, date, time, buildingName, address, categories, uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseStorage", "Error uploading image", e)
                }
        } else {
            Log.d("Firestore", "Image URI is null, blank, or already a Cloud Storage URI, proceeding with Firestore upload")

            // Continue with saving the event data to Firestore
            saveEventDataToFirestore(title, description, date, time, buildingName, address, categories, imageUri ?: "")
        }
    }

    private fun saveEventDataToFirestore(
        title: String,
        description: String,
        date: String?,
        time: String?,
        buildingName: String?,
        address: String?,
        categories: List<String>,
        imageUri: String
    ) {
        // Create a new event document in the "Events" collection
        val event = hashMapOf(
            "title" to title,
            "description" to description,
            "date" to date,
            "time" to time,
            "buildingName" to buildingName,
            "address" to address,
            "imageUri" to imageUri,
            "title" to title,
            "description" to description,
            "date" to date,
            "time" to time,
            "buildingName" to buildingName,
            "address" to address,
            "imageUri" to imageUri,
            "categories" to categories,
            "timestamp" to FieldValue.serverTimestamp()
        )

        // Print out the event details
        Log.d("Firestore", "Event Details:")
        event.forEach { (key, value) ->
            Log.d("Firestore", "$key: $value")
        }

        // Add the event to the "Events" collection
        db.collection("Events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")

                // Start the MapsActivity
                val intent = Intent(this@CreateEventDetailsActivity, MapsActivity::class.java)
                startActivity(intent)

                // Finish the current activity to remove it from the back stack
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding event document", e)
            }
    }
}