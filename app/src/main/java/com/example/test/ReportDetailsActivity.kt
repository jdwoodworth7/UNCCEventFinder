package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ReportDetailsActivity : AppCompatActivity() {

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_reported_event_details)  //sets the content view to main, parent view

        //initialize values from previous activity through intent
        val eventAuthorId = intent.getStringExtra("eventAuthorId").toString()
        val reportAuthorId = intent.getStringExtra("reportAuthorId").toString()
        val reportedDate = intent.getStringExtra("reportedDate").toString()
        val category = intent.getStringExtra("category").toString()
        val details = intent.getStringExtra("details").toString()
        val eventId = intent.getStringExtra("id").toString()
        val eventName = intent.getStringExtra("eventName").toString()


        //get references to TextViews or other views in the content view layout
        val lblEventName : TextView = findViewById(R.id.lblEventName)
        val lblReportDate : TextView = findViewById(R.id.lblReportDate)
        val btnReportCategory : Button = findViewById(R.id.btnCategory)
        val txtReportDetails : EditText = findViewById(R.id.txtReportDetails)

        lblEventName.text = eventName
        lblReportDate.text = reportedDate
        btnReportCategory.text = category
        txtReportDetails.setText(details)

//        runBlocking {
//            if(eventId.isNotEmpty() && authorId.isNotEmpty()){
//                val eventReport = fetchEventReportByAuthorAndEventId(authorId, eventId)
//
//                //set data to the views based on the position in the data source
//                lblEventName.text = eventReport?.eventName
//                lblReportDate.text = eventReport?.reportedDate
//                btnReportCategory.text = eventReport?.category
//                txtReportDetails.setText(eventReport?.details)
//
//            }else{
//                Log.e("Event Report Fetch" , "No such Event or Author ID exists")
//            }
//        }

        // Get a reference to the menuButton
        val menuButton = findViewById<ImageView>(R.id.menuButton)

        // Set a click listener for the menuButton
        menuButton.setOnClickListener {
            // Start the MenuActivity when the menuButton is clicked
            val intent = Intent(this@ReportDetailsActivity, MenuActivity::class.java)
            startActivity(intent)
        }


    }

    suspend fun fetchEventReportByAuthorAndEventId(authorId: String, eventId: String) : EventReport? {
        return withContext(Dispatchers.IO) {
            val eventReportRef = firestore.collection("EventReport")
            val query = eventReportRef
                .whereEqualTo("authorId", authorId)
                .whereEqualTo("eventId", eventId)

            try {
                val querySnapshot = Tasks.await(query.get())
                    val documentSnapshot = querySnapshot.documents[0]
                    val eventReport = documentSnapshot.toObject(EventReport::class.java)
                    eventReport
            }catch (exception: Exception){
                Log.e("Users Data Fetch Request" , "Users with Report Cases Fetch failed: $exception")
                null
            }
        }
    }
}