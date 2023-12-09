package com.example.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.widget.ArrayAdapter;
import java.util.Arrays

class UserProfileActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var listView: ListView
    private var list = ArrayList<String>()
    var userEventsList: MutableList<UserEventsData> = mutableListOf()
    private lateinit var adapter: EventAdapter2
    private var allEvents: List<EventData> = mutableListOf()
    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userprofile)

        val firstnameText: TextView = findViewById(R.id.firstname)
        val status: TextView = findViewById(R.id.status)
        listView = findViewById(R.id.evetnslist)



        //get current userid
        userId = getAuthorIdFromSharedPreferences()
        val userInfo = getUserFromSharedPreferences()

        runOnUiThread {
            // Display user data
            if (userInfo != null) {
                // Display user data
                val firstnameText: TextView = findViewById(R.id.firstname)
                val status: TextView = findViewById(R.id.status)

                // Set the text for firstname and status
                firstnameText.text = userInfo?.firstname
                status.text = userInfo?.status
            } else {
                Log.e("UserProfileActivity", "UserData is null in the intent.")
            }
        }
        // works above


        // Fetch and display event data
        GlobalScope.launch {

            val events = fetchEventsFromUser()
            Log.e("UserProfileActivity", "userID" + userId)
            Log.d("YourTag", "This is what's inside: ${events.size}")
            runOnUiThread {
                if (events.isNotEmpty()) {
                    // User has events, display the list
                    // Initialize the adapter
                    adapter = EventAdapter2(this@UserProfileActivity, events)
                    // Set the adapter to the ListView
                    listView.adapter = adapter
                } else {
                    // User has no events, display a message
                    val noEventsMessage = "No upcoming events"
                    displayNoEventsMessage(noEventsMessage)
                }
            }
        }
    }

    // Function to fetch events from the user and display in a list

    // Main function that fetches events from user
    suspend fun fetchEventsFromUser(): ArrayList<EventData> {
        val events = ArrayList<EventData>()
        val TAG = "-"

        if (userId != null) {
            fetchUserEventsFromUser(userId)
            Log.d(TAG,"this also works")
            Log.d(TAG,"size of usereventlist" + userEventsList.size)
            if (userEventsList.isNotEmpty()) {
                Log.d(TAG,"this is next")
                for (eventData in userEventsList) {
                    Log.d(TAG,"this is please work")
                    val eventId = eventData.eventId

                    // Fetch the event data using eventId
                    val event = fetchEventByEventId(eventId)

                    // If the event exists, add it to the list
                    if (event != null) {
                        events.add(event)
                    }
                }
            }
        }

        return events
    }
    private fun displayNoEventsMessage(message: String) {
        val status: TextView = findViewById(R.id.status)
        listView = findViewById(R.id.evetnslist)
        // Hide the ListView
        listView.visibility = View.GONE

        // Show the TextView and set the message
        status.visibility = View.VISIBLE
        status.text = message
    }


    //gets the userId initialized during login
    private fun getAuthorIdFromSharedPreferences(): String {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val authorId = sharedPreferences.getString("authorId", null)
        return authorId.toString()
    }
    private fun getUserFromSharedPreferences(): UserData {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val id = sharedPreferences.getString("authorId", null).toString()
        val email = sharedPreferences.getString("email", null).toString()
        val username = sharedPreferences.getString("username", null).toString()
        val isModerator = sharedPreferences.getBoolean("isModerator", false)

        val userData = UserData().createUserDataForMenu(id, username, email, isModerator)

        return userData
    }

     private fun fetchUserEventsFromUser(userId: String) {
         Log.d("YourTag", "This with userId: $userId")
         val eventCollectionRef = firestore.collection("UserEvents")
            val query = eventCollectionRef.whereEqualTo("userId", userId)
            userEventsList.clear()
            try {
                val querySnapshot = Tasks.await(query.get())
                val userEvents = querySnapshot.toObjects(UserEventsData::class.java)
                for (event in userEvents) {

                    userEventsList.add(event)
                }

            } catch (exception: Exception) {
                Log.e("UserEvent FetchById Request", "UserEvent Fetch Failed: $exception")
            }
        Log.d("YourTag", "YYO OVER HERE: ${userEventsList.size}")
        }

    /////This should help you fix the name problem
    /*
    suspend fun fetchUserEventsFromUser(userId: String) {
        return withContext(Dispatchers.IO) {
            val eventCollectionRef = firestore.collection("UserEvents")
            val query = eventCollectionRef.whereEqualTo("userId", userId)

            userEventsDataList.clear()

            try {
                val querySnapshot = Tasks.await(query.get())
                val userEvents = querySnapshot.toObjects(UserEventsData::class.java)
                userEvents.forEach { userEvent ->
                    userEventsDataList.add(userEvent) //updates global variable userEventsDataList
                }
            } catch (exception: Exception) {
                Log.e("UserEvent FetchById Request", "UserEvent Fetch Failed: $exception")
            }
        }
    }
     */


    suspend fun fetchEventByEventId(eventId: String): EventData? {
        return try {
            withContext(Dispatchers.IO) {

                val collectionRef = firestore.collection("Events")
                val query = collectionRef.document(eventId)

                val querySnapshot = Tasks.await(query.get())
                querySnapshot.toObject(EventData::class.java)!!
            }
        } catch (exception: Exception) {
            Log.e("Event FetchById Request", "Event Fetch Failed: " + exception)
            null
        }
    }

    ///This gets the event details
    private suspend fun fetchEventDataFireStore(): List<EventData> {
        // Implement your code to fetch event data from Firestore
        val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
        val eventCollectionRef = firestore.collection("Events")
        val eventList = mutableListOf<EventData>()

        try {
            val querySnapshot = eventCollectionRef.get().await()
            for (document in querySnapshot) {
                //eventId is not the id assigned to the data inside event (ID field),
                //but a id that is assigned to the document which contains the event data
                val currentEventData = document.toObject(EventData::class.java)
                currentEventData.id = document.id
                eventList.add(currentEventData)

            }
        } catch (e: Exception) {
            Log.e(
                "Reference Fetch Failure",
                "Failed to fetch docuemnts from the collection reference: " + e
            )
        }

        return eventList
    }


  /*  //the thing to show the upcomign events
    private fun displayEventsOverlay(event: EventData) {
        //inflates overlay layout
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val overlayView = inflater.inflate(R.layout.upcomingactivites, null)


        //finds and stores views from overlay layout
        val titleTextView: TextView = overlayView.findViewById(R.id.titletext)
        val addressTextView: TextView = overlayView.findViewById(R.id.eventdetails)
        val imageView: ImageView = overlayView.findViewById(R.id.eventimage)

        //assign parameter for the new layout
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) //align to bottom of parent
        overlayView.layoutParams = params

       // currentOverlayView = overlayView

        titleTextView.text = event.title
        addressTextView.text = event.address
        //load event image
        imageView.load(event.imageUri)

    }

    \*
   */

    // Event datafetcher
    private fun fetchEventByFireStoreID(id: String, callback: (EventData?) -> Unit) {
        val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
        val eventRef = firestore.collection("Events").document(id)

        eventRef.get()
            .addOnSuccessListener { querySnapShot ->
                if (querySnapShot.exists()) {
                    val eventData = querySnapShot.toObject(EventData::class.java)
                    callback(eventData)
                } else {
                    Log.e("Event FetchById Request", "Following event does not exist")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Event FetchById Request", "Event Fetch Failed: " + exception)
            }
    }
}
