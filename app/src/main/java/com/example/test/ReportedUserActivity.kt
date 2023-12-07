package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ReportedUserActivity : AppCompatActivity() {

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_reports_by_user)  //sets the content view to main, parent view

        //initialize values from previous activity through intent
        val authorId = intent.getStringExtra("id")
        val firstName = intent.getStringExtra("firstname")
        val lastName = intent.getStringExtra("lastname")

        val authorName: String = "$firstName $lastName"

        val listView = findViewById<ListView>(R.id.reportedEventsListView) //defines list view to populate data into it

        runBlocking {
            val userReportedEvents = fetchAuthorEventsWithReports(authorId)  //fetches events by selected user with report

            val adapter = ReportListAdapter(this@ReportedUserActivity,userReportedEvents, authorName)
            listView.adapter = adapter //gets the view based on adapter options
        }

        // Get a reference to the menuButton
        val menuButton = findViewById<ImageView>(R.id.menuButton)

        // Set a click listener for the menuButton
        menuButton.setOnClickListener {
            // Start the MenuActivity when the menuButton is clicked
            val intent = Intent(this@ReportedUserActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    //fetches all events hosted by the user with filter with reports only
    suspend fun fetchAuthorEventsWithReports (authorId : String?) : MutableList<EventData> {
        return withContext(Dispatchers.IO){
            val eventCollectionRef = firestore.collection("Events")

            //queries for all events with author id equals to AND report count > 0
            val query = eventCollectionRef
                .whereEqualTo("authorId" , authorId)
                .whereGreaterThan("reportCount", 0)

            try {
                val querySnapshot = Tasks.await(query.get())
                val reportedEvents = querySnapshot.toObjects(EventData::class.java)
                reportedEvents //returns reported events
            } catch (exception: Exception) {
                Log.e("Events Data Fetch Request" , "fetchAuthorEventsWithReports Failed: $exception")
                mutableListOf() //returns empty mutable list
            }
        }
    }
}