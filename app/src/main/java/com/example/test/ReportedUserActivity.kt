package com.example.test

import android.os.Bundle
import android.util.Log
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

        val authorId = intent.getStringExtra("id")
        val firstName = intent.getStringExtra("firstname")
        val lastName = intent.getStringExtra("lastname")
        val email = intent.getStringExtra("email")
        val reportCases = intent.getStringExtra("reportCases")

        val listView = findViewById<ListView>(R.id.reportedEventsListView) //defines list view to populate data into it

        runBlocking {
            val userReportedEvents = fetchAuthorEventsWithReports(authorId)

            val adapter = ReportListAdapter(this@ReportedUserActivity,userReportedEvents)
            listView.adapter = adapter //gets the view based on adapter options
        }
    }

    //fetches all events hosted by the user with filter with reports only
    suspend fun fetchAuthorEventsWithReports (authorId : String?) : MutableList<EventData> {
        return withContext(Dispatchers.IO){
            val eventCollectionRef = firestore.collection("Users")
            val query = eventCollectionRef
                .whereEqualTo("authorId" , authorId)
                .whereGreaterThan("reportCount", 0)

            try {
                val querySnapshot = Tasks.await(query.get())
                val reportedEvents = querySnapshot.toObjects(EventData::class.java)
                reportedEvents
            } catch (exception: Exception) {
                Log.e("Events Data Fetch Request" , "fetchAuthorEventsWithReports Failed: $exception")
                mutableListOf()
            }
        }
    }
}