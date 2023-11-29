package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CreateEventCategoriesActivity : AppCompatActivity() {
    private lateinit var sessionsList: Array<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_categories)

        val checkBoxAcademic = findViewById<CheckBox>(R.id.checkboxAcademic)
        val checkBoxSocial = findViewById<CheckBox>(R.id.checkboxSocial)
        val checkBoxSports = findViewById<CheckBox>(R.id.checkboxSports)
        val checkBoxClubsOrg = findViewById<CheckBox>(R.id.checkboxClubsOrg)
        val checkBoxWorkshops = findViewById<CheckBox>(R.id.checkboxWorkshops)
        val checkBoxVolunteering = findViewById<CheckBox>(R.id.checkboxVolunteering)
        val checkBoxStudentsOnly = findViewById<CheckBox>(R.id.checkboxStudentsOnly)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)

        // Retrieve the sessionsList from the intent
        sessionsList = intent.getSerializableExtra("sessionsList") as? Array<Array<String>> ?: emptyArray()

        continueButton.setOnClickListener {
            // Retrieve data from the previous activity
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")
            val buildingName = intent.getStringExtra("buildingName")
            val address = intent.getStringExtra("address")
            val imageUri = intent.getStringExtra("imageUri")

            val intent = Intent(this@CreateEventCategoriesActivity, CreateEventAudienceActivity::class.java)

            // Pass the data to the next activity
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            intent.putExtra("buildingName", buildingName)
            intent.putExtra("address", address)
            intent.putExtra("imageUri", imageUri)

            // Pass checkbox information
            intent.putExtra("checkboxAcademic", checkBoxAcademic.isChecked)
            intent.putExtra("checkboxSocial", checkBoxSocial.isChecked)
            intent.putExtra("checkboxSports", checkBoxSports.isChecked)
            intent.putExtra("checkboxClubsOrg", checkBoxClubsOrg.isChecked)
            intent.putExtra("checkboxWorkshops", checkBoxWorkshops.isChecked)
            intent.putExtra("checkboxVolunteering", checkBoxVolunteering.isChecked)
            intent.putExtra("checkboxStudentsOnly", checkBoxStudentsOnly.isChecked)

            // Pass the sessionsList to the next activity
            intent.putExtra("sessionsList", sessionsList)

            // Start the next activity
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            // If the user clicks the "Cancel" button, finish the activity
            finish()
        }

        menuButton.setOnClickListener {
            // Open the menu activity when the menu button is clicked
            val intent = Intent(this@CreateEventCategoriesActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        mapIcon.setOnClickListener {
            // Open the map activity when the map button is clicked
            val intent = Intent(this@CreateEventCategoriesActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}