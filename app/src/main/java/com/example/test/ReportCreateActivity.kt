package com.example.test

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate

class ReportCreateActivity : AppCompatActivity() {

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
    private lateinit var selectedCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_create)  //sets the content view to main, parent view

        //initialize value with parcelable EventData object
        val selectedEvent = intent.getParcelableExtra<EventData>("selectedEvent")

        //allocate necessary event values to the text view
        val eventName: TextInputEditText = findViewById(R.id.inputReportEventName)
        val reportCategories: Button = findViewById(R.id.inputReportEventReason)
        val reportInput: TextInputEditText = findViewById(R.id.inputReportEventDetail)
        val submitButton: Button = findViewById(R.id.btnSubmitReports)

        //set the title of the current report to selected event's title
        eventName.setText(selectedEvent?.title)

        //disable the editText so that event name appears static
        eventName.isEnabled = false

        //builds the AlertDialog for category options
        reportCategories.setOnClickListener {
            val options = arrayOf("Misleading Information", "Inappropriate Content", "Duplicate Event", "Expired or Outdated Event", "Others")

            val builder = AlertDialog.Builder(this)
            builder.setItems(options) { _, which ->
                selectedCategory = options[which]
                reportCategories.text = selectedCategory
            }

            builder.create().show()
        }

        //initialize a variable to handle user input changes
        var userInput : String? = null

        //handles textChanged event to listen to user input
        reportInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text in the input field has been changed
                userInput = s.toString()
                // Perform actions with the updated userInput here
            }
        })

        //when submit report button is clicked
        submitButton.setOnClickListener{
            //checks to see if user has details fixed
            if(!userInput.isNullOrEmpty()){
                postReportDataToFireStore(selectedEvent, userInput!!)
                Toast.makeText(this, "Report has been successfully posted. We thank you for your report", Toast.LENGTH_SHORT).show()
                finish()
            } else if(userInput.isNullOrEmpty()){
                Toast.makeText(this, "You cannot submit your report without details. Please try again", Toast.LENGTH_SHORT).show()
            } else if(selectedCategory.isNullOrEmpty()){
                Toast.makeText(this, "You cannot submit your report without selecting report category. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postReportDataToFireStore(selectedEvent: EventData?, inputText: String) {

        val eventReport = EventReport(
            id = "",
            eventAuthorId = selectedEvent?.authorId.toString(),
            reportAuthorId = getUserIdFromSharedPreferences(),
            reportedDate = LocalDate.now().toString(),
            category = selectedCategory,
            details = inputText,
            eventId = selectedEvent?.id.toString(),
            eventName = selectedEvent?.title.toString()
        )

        val eventReportRef = firestore.collection("EventReport")

        //adds the eventReport to the FireStore database
        eventReportRef.add(eventReport)
            .addOnSuccessListener { documentRef ->
                //initialize the Firebase generated document Id to store as a field
                val generatedId = documentRef.id
                val updatedEventReport = eventReport.copy(id = generatedId)

                //log the successful POST
                Log.d("Firebase POST", "EventReport added with ID: ${generatedId}")

                documentRef.set(updatedEventReport)
                    .addOnSuccessListener {
                        Log.d("Firebase PUT", "EventReport updated with ID: ${generatedId}")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firebase PUT", "Error updating to EventReport: $exception")

                    }.addOnFailureListener { exception ->
                        Log.e("Firebase POST", "Error adding to EventReport: $exception")
                    }
            }


    }

    private fun getUserIdFromSharedPreferences(): String {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val authorId = sharedPreferences.getString("authorId", null)
        return authorId.toString()
    }

}