package com.example.test

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

class CreateEventSessionsActivity : AppCompatActivity() {

    private lateinit var selectedTimeStart: LocalTime
    private lateinit var selectedTimeEnd: LocalTime
    private lateinit var datePickerDialogStart: DatePickerDialog
    private lateinit var datePickerDialogEnd: DatePickerDialog
    private lateinit var timePickerDialogStart: TimePickerDialog
    private lateinit var timePickerDialogEnd: TimePickerDialog
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
            val startDate = firstDateButton.text.toString()
            val startTime = firstTimeButton.text.toString()
            val endDate = secondDateButton.text.toString()
            val endTime = secondTimeButton.text.toString()

            // Check if any field is empty
            if (startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty()) {
                // Show a toast message indicating that all fields must be filled out
                Toast.makeText(this@CreateEventSessionsActivity, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Combine start date and time
                val startDateTime = parseDateTime("$startDate $startTime")

                // Combine end date and time
                val endDateTime = parseDateTime("$endDate $endTime")

                // Check if startDateTime is after endDateTime
                if (startDateTime.isAfter(endDateTime)) {
                    // Show a toast message indicating the error
                    Toast.makeText(this@CreateEventSessionsActivity, "Start date and time cannot be after end date and time", Toast.LENGTH_SHORT).show()
                } else {
                    // Create a session array and add it to the sessionsList
                    val session = listOf(startDate, startTime, endDate, endTime)
                    sessionsList.add(session)

                    // Update the currentSessions TextView with the formatted array
                    updateCurrentSessionsTextView(currentSessionsTextView)
                }
            }
        }

        continueButton.setOnClickListener {
            // Check if sessionsList is not empty
            if (sessionsList.isNotEmpty()) {
                // Retrieve data from the previous activity
                val title = intent.getStringExtra("title")
                val description = intent.getStringExtra("description")
                val buildingName = intent.getStringExtra("buildingName")
                val address = intent.getStringExtra("address")
                val imageUri = intent.getStringExtra("imageUri")

                val intent = Intent(this@CreateEventSessionsActivity, CreateEventCategoriesActivity::class.java)

                // Pass the data to the next activity
                intent.putExtra("title", title)
                intent.putExtra("description", description)
                intent.putExtra("buildingName", buildingName)
                intent.putExtra("address", address)
                intent.putExtra("imageUri", imageUri)

                // Pass the sessionsList to the next activity
                intent.putExtra("sessionsList", sessionsList.map { it.toTypedArray() }.toTypedArray())

                // Start the next activity
                startActivity(intent)
            } else {
                // Show a toast message indicating that sessionsList is empty
                Toast.makeText(this@CreateEventSessionsActivity, "Please add at least one session", Toast.LENGTH_SHORT).show()
            }
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
        val date = LocalDate.of(year, month, day)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        return date.format(formatter)
    }

    private fun openTimePickerStart() {
        val cal = Calendar.getInstance()
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        timePickerDialogStart = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                selectedTimeStart = LocalTime.of(hour, minute)

                val timeString = makeTimeString(hour, minute)
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

        timePickerDialogEnd = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                selectedTimeEnd = LocalTime.of(hour, minute)

                val timeString = makeTimeString(hour, minute)
                secondTimeButton.text = timeString
            },
            hourOfDay,
            minute,
            false
        )

        timePickerDialogEnd.show()
    }

    private fun makeTimeString(hour: Int, minute: Int): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
        return LocalTime.of(hour, minute).format(formatter)
    }

    // Parse the date and time into LocalDateTime for comparison
    private fun parseDateTime(dateTimeString: String): LocalDateTime {
        val trimmedString = dateTimeString.trim()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
        return try {
            LocalDateTime.parse(trimmedString, formatter)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            LocalDateTime.now()
        }
    }

}