package com.example.test // Change this package name to match your app's package

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //return the UserData object after fetching from shared preference
        val userInfo = getUserFromSharedPreferences()

        val userNameView : TextView = findViewById(R.id.studentName)
        val userEmailView : TextView = findViewById(R.id.studentEmail)

        //set the text view by UserData object field values
        userNameView.text = userInfo.firstname
        userEmailView.text = userInfo.email

        // Find the "Create an Event" TextView
        val createEventTextView: TextView = findViewById(R.id.createEvent)

        // Set an onClickListener for "Create an Event"
        createEventTextView.setOnClickListener {

            // Launch CreateEventActivity
            val intent = Intent(this@MenuActivity, CreateEventActivity::class.java)
            startActivity(intent)
        }

        // Find the "Search Event" TextView
        val searchEventTextView: TextView = findViewById(R.id.searchEvent)

        // Set an onClickListener for "Search Event"
        searchEventTextView.setOnClickListener {
            // Launch SearchActivity
            val intent = Intent(this@MenuActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Find the "My Schedules" TextView
        val calendarViewTextView: TextView = findViewById(R.id.mySchedules)

        // Set an onClickListener for "My Schedules"
        calendarViewTextView.setOnClickListener {

            // Launch CalendarViewActivity
            val intent = Intent(this@MenuActivity, CalendarViewActivity::class.java)
            startActivity(intent)
        }

        // Find the "Setting" TextView
        val settings: TextView = findViewById(R.id.settings)

        // Set an onClickListener for "Setting"
        settings.setOnClickListener {

            // Launch PrivacySettingsActivity
            val intent = Intent(this@MenuActivity, PrivacySettingsActivity::class.java)
            startActivity(intent)
        }

        // Find the "Admin View" TextView

        val btnAdminView : Button = findViewById(R.id.btnAdminView)

        if(userInfo.isModerator) {
            btnAdminView.isVisible = true
        }

        // Set an onClickListener for "btnAdminView"
        btnAdminView.setOnClickListener {

            // Launch ReportActivity
            val intent = Intent(this@MenuActivity, ReportActivity::class.java)
            startActivity(intent)
        }
        // Find the "My Schedules" TextView
        val myProfileView: TextView = findViewById(R.id.myProfile)

        // Set an onClickListener for "My Schedules"
        myProfileView.setOnClickListener {

            // Launch UserProfileActivity
            val intent = Intent(this@MenuActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }
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
}