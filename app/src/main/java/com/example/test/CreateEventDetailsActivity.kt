package com.example.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

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
        val buildingName = intent.getStringExtra("buildingName")
        val address = intent.getStringExtra("address")
        val imageUri = intent.getStringExtra("imageUri")

        // Build a string to display checkbox details
        val categoriesCheckBoxDetails = buildCategoriesCheckBoxDetails()
        val audienceCheckBoxDetails = buildAudienceCheckBoxDetails()

        // Retrieve the sessions list from the intent
        val sessionsList = intent.getSerializableExtra("sessionsList") as? Array<Array<String>> ?: emptyArray()
        // Build string representation for sessions list
        val sessionsDetails = buildSessionsDetails(sessionsList)

        // Display the data in detailsEditText
        val detailsEditText = findViewById<EditText>(R.id.detailsEditText)
        detailsEditText.setText(
            "You have created an event!\n\n" +
                    "Title: $title\n" +
                    "Description: $description\n" +
                    "Building Name: $buildingName\n" +
                    "Address: $address\n" +
                    "Categories: $categoriesCheckBoxDetails\n" +
                    "Audiences: $audienceCheckBoxDetails\n" +
                    "Image URI: $imageUri\n" +
                    "Sessions:\n$sessionsDetails"
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

            // Retrieve checkbox states for audiences from the intent
            val checkBoxUndergrad = intent.getBooleanExtra("checkboxUndergrad", false)
            val checkBoxGrad = intent.getBooleanExtra("checkboxGrad", false)
            val checkBoxFacultyStaff = intent.getBooleanExtra("checkboxFacultyStaff", false)
            val checkBoxAlumni = intent.getBooleanExtra("checkboxAlumni", false)
            val checkBoxPublicCommunity = intent.getBooleanExtra("checkboxPublicCommunity", false)
            val checkBoxFamily = intent.getBooleanExtra("checkboxFamily", false)
            val checkBoxProspecStudents = intent.getBooleanExtra("checkboxProspecStudents", false)

            // Create a list of audiences based on checkbox states
            val audience = mutableListOf<String>()
            if (checkBoxUndergrad) audience.add("Undergraduate Students")
            if (checkBoxGrad) audience.add("Graduate Students")
            if (checkBoxFacultyStaff) audience.add("Faculty & Staff")
            if (checkBoxAlumni) audience.add("Alumni")
            if (checkBoxPublicCommunity) audience.add("Public & Community")
            if (checkBoxFamily) audience.add("Family")
            if (checkBoxProspecStudents) audience.add("Prospective Students")

            // Save event details to Firestore and upload image to Storage
            saveEventToFirestore(
                title ?: "",
                description ?: "",
                sessionsList,
                buildingName ?: "",
                address ?: "",
                categories ?: emptyList(),  // Provide an empty list if null
                audience ?: emptyList(),
                imageUri ?: ""
            )
        }
    }

    private fun buildCategoriesCheckBoxDetails(): String {
        val categoriesCheckBoxDetails = StringBuilder()

        // Retrieve checkbox states from the intent
        val checkBoxAcademic = intent.getBooleanExtra("checkboxAcademic", false)
        val checkBoxSocial = intent.getBooleanExtra("checkboxSocial", false)
        val checkBoxSports = intent.getBooleanExtra("checkboxSports", false)
        val checkBoxClubsOrg = intent.getBooleanExtra("checkboxClubsOrg", false)
        val checkBoxWorkshops = intent.getBooleanExtra("checkboxWorkshops", false)
        val checkBoxVolunteering = intent.getBooleanExtra("checkboxVolunteering", false)
        val checkBoxStudentsOnly = intent.getBooleanExtra("checkboxStudentsOnly", false)

        if (checkBoxAcademic) categoriesCheckBoxDetails.append("Academic, ")
        if (checkBoxSocial) categoriesCheckBoxDetails.append("Social, ")
        if (checkBoxSports) categoriesCheckBoxDetails.append("Sports, ")
        if (checkBoxClubsOrg) categoriesCheckBoxDetails.append("Clubs/Organizations, ")
        if (checkBoxWorkshops) categoriesCheckBoxDetails.append("Workshops/Seminars, ")
        if (checkBoxVolunteering) categoriesCheckBoxDetails.append("Volunteering, ")
        if (checkBoxStudentsOnly) categoriesCheckBoxDetails.append("Students Only, ")

        // Remove the trailing comma and space
        return categoriesCheckBoxDetails.toString().removeSuffix(", ")
    }

    private fun buildAudienceCheckBoxDetails(): String {
        val audienceCheckBoxDetails = StringBuilder()

        // Retrieve checkbox states from the intent
        val checkBoxUndergrad = intent.getBooleanExtra("checkboxUndergrad", false)
        val checkBoxGrad = intent.getBooleanExtra("checkboxGrad", false)
        val checkBoxFacultyStaff = intent.getBooleanExtra("checkboxFacultyStaff", false)
        val checkBoxAlumni = intent.getBooleanExtra("checkboxAlumni", false)
        val checkBoxPublicCommunity = intent.getBooleanExtra("checkboxPublicCommunity", false)
        val checkBoxFamily = intent.getBooleanExtra("checkboxFamily", false)
        val checkBoxProspecStudents = intent.getBooleanExtra("checkboxProspecStudents", false)

        if (checkBoxUndergrad) audienceCheckBoxDetails.append("Undergraduate Students, ")
        if (checkBoxGrad) audienceCheckBoxDetails.append("Graduate Students, ")
        if (checkBoxFacultyStaff) audienceCheckBoxDetails.append("Faculty & Staff, ")
        if (checkBoxAlumni) audienceCheckBoxDetails.append("Alumni, ")
        if (checkBoxPublicCommunity) audienceCheckBoxDetails.append("Public & Community, ")
        if (checkBoxFamily) audienceCheckBoxDetails.append("Family, ")
        if (checkBoxProspecStudents) audienceCheckBoxDetails.append("Prospective Students, ")

        // Remove the trailing comma and space
        return audienceCheckBoxDetails.toString().removeSuffix(", ")
    }

    private fun saveEventToFirestore(
        title: String,
        description: String,
        sessionsList: Array<Array<String>>,
        buildingName: String?,
        address: String?,
        categories: List<String>,
        audience: List<String>,
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
                        saveEventDataToFirestore(title, description, sessionsList, buildingName, address, categories, audience, uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseStorage", "Error uploading image", e)
                }
        } else {
            Log.d("Firestore", "Image URI is null, blank, or already a Cloud Storage URI, proceeding with Firestore upload")

            // Continue with saving the event data to Firestore
            saveEventDataToFirestore(title, description, sessionsList, buildingName, address, categories, audience, imageUri ?: "")
        }
    }

    private fun saveEventDataToFirestore(
        title: String,
        description: String,
        sessionsList: Array<Array<String>>,
        buildingName: String?,
        address: String?,
        categories: List<String>,
        audience: List<String>,
        imageUri: String
    ) {
        Log.d("Firestore", "Saving event data to Firestore")

        // Save EventSessionData to Firestore and get the generated IDs
        saveEventSessionsToFirestore(sessionsList) { eventSessionIds ->
            // Create a new event document in the "Events" collection
            val event = hashMapOf(
                "title" to title,
                "description" to description,
                "eventSessionIds" to eventSessionIds,
                "buildingName" to buildingName,
                "address" to address,
                "imageUri" to imageUri,
                "categories" to categories,
                "audience" to audience,
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

    private fun saveEventSessionsToFirestore(
        sessionsList: Array<Array<String>>,
        onComplete: (List<String>) -> Unit
    ) {
        // Save EventSessionData to EventSessions collection and get the generated IDs
        val eventSessionIds = mutableListOf<String>()

        // Create a list to hold all the task references
        val tasks = mutableListOf<Task<Void>>()

        sessionsList.forEachIndexed { index, session ->
            val eventSessionData = hashMapOf(
                "startDate" to session[0],
                "startTime" to session[1],
                "endDate" to session[2],
                "endTime" to session[3]
            )

            // Get randomly generated document names
            val documentName = UUID.randomUUID().toString()

            // Add the event session to the "EventSessions" collection with the specified document name
            val task = db.collection("EventSessions")
                .document(documentName)
                .set(eventSessionData)
                .addOnSuccessListener {
                    Log.d("Firestore", "EventSessionData added with ID: $documentName")
                    eventSessionIds.add(documentName)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding event session document", e)
                }

            // Add the task to the list
            tasks.add(task)
        }

        // Use Tasks.whenAllSuccess to wait for all tasks to complete
        Tasks.whenAllSuccess<Void>(tasks)
            .addOnSuccessListener {
                Log.d("Firestore", "All tasks completed successfully")
                onComplete(eventSessionIds)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "One or more tasks failed", e)
            }
            .addOnCompleteListener {
                // Do something if needed after all tasks complete
            }
    }

    private fun buildSessionsDetails(sessionsList: Array<Array<String>>): String {
        val sessionsDetails = StringBuilder()

        sessionsList.forEachIndexed { index, session ->
            val sessionDetails = session.joinToString(", ")
            sessionsDetails.append("${index + 1}. $sessionDetails\n")
        }

        return sessionsDetails.toString()
    }
}