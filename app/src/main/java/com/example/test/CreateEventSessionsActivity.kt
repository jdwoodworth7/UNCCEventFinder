package com.example.test

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

class CreateEventSessionsActivity : AppCompatActivity() {

    private lateinit var selectedTimeStart: LocalTime
    private lateinit var selectedTimeEnd: LocalTime
    private lateinit var datePickerDialogStart: DatePickerDialog
    private lateinit var datePickerDialogEnd: DatePickerDialog
    private lateinit var firstDateButton: Button
    private lateinit var secondDateButton: Button
    private lateinit var firstTimeButton: Button
    private lateinit var secondTimeButton: Button
    private val sessionsList = mutableListOf<List<String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_sessions)

        val submitButton = findViewById<Button>(R.id.submitButton)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        val startDateButton = findViewById<Button>(R.id.startDateButton)
        val startTimeButton = findViewById<Button>(R.id.startTimeButton)
        val endDateButton = findViewById<Button>(R.id.endDateButton)
        val endTimeButton = findViewById<Button>(R.id.endTimeButton)
        val currentSessionsTextView = findViewById<TextView>(R.id.currentSessions)

        firstDateButton = findViewById(R.id.startDateButton)
        secondDateButton = findViewById(R.id.endDateButton)
        firstTimeButton = findViewById(R.id.startTimeButton)
        secondTimeButton = findViewById(R.id.endTimeButton)

        firstDateButton.setOnClickListener {
            openDatePickerStart()
        }

        secondDateButton.setOnClickListener {
            openDatePickerEnd()
        }

        firstTimeButton.setOnClickListener {
            openTimePickerStart()
        }

        secondTimeButton.setOnClickListener {
            openTimePickerEnd()
        }

        submitButton.setOnClickListener {
            // Get values from EditText fields
            val startDate = startDateButton.text.toString()
            val startTime = startTimeButton.text.toString()
            val endDate = endDateButton.text.toString()
            val endTime = endTimeButton.text.toString()

            // Create a session array and add it to the sessionsList
            val session = listOf(startDate, startTime, endDate, endTime)
            sessionsList.add(session)

            // Update the currentSessions TextView with the formatted array
            updateCurrentSessionsTextView(currentSessionsTextView)
        }

        continueButton.setOnClickListener {
            // Retrieve data from the previous activity
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")
            val buildingName = intent.getStringExtra("buildingName")
            val address = intent.getStringExtra("address")
            val imageUri = intent.getStringExtra("imageUri")

            val intent = Intent(this@CreateEventSessionsActivity, CreateEventCategoriesActivity::class.java)

            // Pass the data to the next activity
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            intent.putExtra("buildingName", buildingName)
            intent.putExtra("address", address)
            intent.putExtra("imageUri", imageUri)

            // Pass the sessionsList to the next activity
            intent.putExtra("sessionsList", sessionsList.map { it.toTypedArray() }.toTypedArray())

            // Start the next activity
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            // If the user clicks the "Cancel" button, finish the activity
            finish()
        }

        menuButton.setOnClickListener {
            // Open the menu activity when the menu button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        mapIcon.setOnClickListener {
            // Open the map activity when the map button is clicked
            val intent = Intent(this@CreateEventSessionsActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateCurrentSessionsTextView(textView: TextView) {
        // Format the sessionsList and update the TextView
        val formattedSessions = sessionsList.joinToString("\n") { session ->
            session.joinToString(", ")
        }
        textView.text = formattedSessions
    }

    private fun openDatePickerStart() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialogStart = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // set the selectedDate to the user input value in LocalDate
                selectedDate = LocalDate.of(year, month + 1, day)

                val dateString = makeDateString(day, month + 1, year)
                firstDateButton.text = dateString
            },
            year,
            month,
            day
        )

        datePickerDialogStart.show()
    }

    private fun openDatePickerEnd() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialogEnd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // set the selectedDate to the user input value in LocalDate
                selectedDate = LocalDate.of(year, month + 1, day)

                val dateString = makeDateString(day, month + 1, year)
                secondDateButton.text = dateString
            },
            year,
            month,
            day
        )

        datePickerDialogEnd.show()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN"
        }
    }

    private fun openTimePickerStart() {
        val cal = Calendar.getInstance()
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialogStart = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                // set the selectedTime to the user input value in LocalTime
                selectedTimeStart = LocalTime.of(hour, minute)

                // Convert 24-hour format to 12-hour format with AM/PM
                val amPm = if (hour < 12) "AM" else "PM"
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                val timeString = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)

                // Update the text of the timeButton
                firstTimeButton.text = timeString
            },
            hourOfDay,
            minute,
            false
        )

        timePickerDialogStart.show()
    }

    private fun openTimePickerEnd() {
        val cal = Calendar.getInstance()
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialogEnd = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                // set the selectedTime to the user input value in LocalTime
                selectedTimeEnd = LocalTime.of(hour, minute)

                // Convert 24-hour format to 12-hour format with AM/PM
                val amPm = if (hour < 12) "AM" else "PM"
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                val timeString = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)

                // Update the text of the timeButton
                secondTimeButton.text = timeString
            },
            hourOfDay,
            minute,
            false
        )

        timePickerDialogEnd.show()
    }

}