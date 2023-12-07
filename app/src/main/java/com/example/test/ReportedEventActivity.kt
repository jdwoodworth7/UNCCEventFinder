package com.example.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ReportedEventActivity : AppCompatActivity() {

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_reported_events)  //sets the content view to main, parent view

        val listView = findViewById<ListView>(R.id.reportListView) //defines list view to populate data into it

        //initialize values from previous activity through intent - ReportedListAdapter/ReportedUserActivity
        val eventId = intent.getStringExtra("id").toString()

        runBlocking {
            val eventReports = fetchEventReportsByEventId(eventId)!!
            if(eventReports.isNotEmpty()){
                val adapter = ReportedEventAdapter(this@ReportedEventActivity, eventReports)

                listView.adapter = adapter
            }
        }

    }

    suspend fun fetchEventReportsByEventId(eventId: String): MutableList<EventReport>? {
        return withContext(Dispatchers.IO) {
            val eventReportReference = firestore.collection("EventReport")
            val query = eventReportReference.whereEqualTo("eventId", eventId)

            try {
                val querySnapshot = Tasks.await(query.get())
                val eventReports = querySnapshot.toObjects(EventReport::class.java)
                eventReports
            } catch (exception: Exception) {
                Log.e("EventReport Fetch Request" , "Event report fetch with eventId failed: $exception")
                null
            }

        }
    }
}