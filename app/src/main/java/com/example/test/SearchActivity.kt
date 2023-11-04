package com.example.test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Intent
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Check if the fragment container exists before adding the fragment
        if (findViewById<View>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }

            // Request permission when the activity is created
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

            val searchResultsFragment = SearchResultsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, searchResultsFragment)
                .commit()
        }

        // Find the menu button and set a click listener
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@SearchActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the map icon and set a click listener
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        mapIcon.setOnClickListener {
            // Open the MapActivity when the map icon is clicked
            val intent = Intent(this@SearchActivity, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted, you can proceed with the image loading logic
                // For example, reload your fragment or perform the image loading action
                loadImages()
            } else {
                // Permission is not granted, handle it accordingly
                // You might want to show a message to the user or disable features that require the permission
            }
        }

    // This method contains the image loading logic
    private fun loadImages() {
        // Implement your image loading logic here
    }
}