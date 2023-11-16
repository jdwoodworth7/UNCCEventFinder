package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import android.view.inputmethod.EditorInfo

class SearchActivity : AppCompatActivity() {

    private val FILTER_REQUEST_CODE = 1
    private lateinit var searchEditText: EditText
    private val filterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Retrieve filter data from the result
            val filterData = result.data?.getParcelableExtra<FilterData>("filterData")

            // Apply the filter to the fragment
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (fragment is SearchResultsFragment && filterData != null) {
                fragment.applyFilter(filterData)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Check if the fragment container exists before adding the fragment
        if (findViewById<View>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }

            // Request permission when the activity is created
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            val searchResultsFragment = SearchResultsFragment()

            supportFragmentManager.commit {
                add(R.id.fragment_container, searchResultsFragment)
            }
        }

        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
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

        // Find the filter icon and set a click listener
        val filterIcon = findViewById<ImageView>(R.id.filterIcon)
        filterIcon.setOnClickListener {
            // Open the FilterActivity when the filter icon is clicked
            filterLauncher.launch(Intent(this@SearchActivity, FilterActivity::class.java))
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

    private fun performSearch() {
        val searchQuery = searchEditText.text.toString().trim()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment is SearchResultsFragment) {
            fragment.searchEventsByTitle(searchQuery)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve filter data from the result
            val filterData = data?.getParcelableExtra<FilterData>("filterData")

            // Apply the filter to the fragment
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (fragment is SearchResultsFragment && filterData != null) {
                fragment.applyFilter(filterData)
            }
        }
    }

}