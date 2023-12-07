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

        // Get a reference to the menuButton
        val menuButton = findViewById<ImageView>(R.id.menuButton)

        // Set a click listener for the menuButton
        menuButton.setOnClickListener {
            // Start the MenuActivity when the menuButton is clicked
            val intent = Intent(this@ReportDetailsActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}