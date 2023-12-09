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

class SearchUsersActivity : AppCompatActivity() {

    // Request code for launching the filter activity
    private val FILTER_REQUEST_CODE = 1

    // EditText for entering search queries
    private lateinit var searchEditText: EditText

    // Activity result launcher for handling filter activity results
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
        setContentView(R.layout.activity_searchusers)

        // Check if the fragment container exists before adding the fragment
        if (findViewById<View>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }

            // Create and add the SearchResultsFragment to the fragment container
            val searchResultsFragment = SearchResultsFragment()

            supportFragmentManager.commit {
                add(R.id.fragment_container, searchResultsFragment)
            }
        }

        // Set up the searchEditText for search queries
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        // Set up click listeners for menu, map, and filter icons

        // Find the menu button and set a click listener
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@SearchUsersActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the map icon and set a click listener
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        mapIcon.setOnClickListener {
            // Open the MapActivity when the map icon is clicked
            val intent = Intent(this@SearchUsersActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        // Find the filter icon and set a click listener
        val filterIcon = findViewById<ImageView>(R.id.filterIcon)
        filterIcon.setOnClickListener {
            // Open the FilterActivity when the filter icon is clicked
            filterLauncher.launch(Intent(this@SearchUsersActivity, FilterActivity::class.java))
        }
    }

    // Perform search based on the entered query
    private fun performSearch() {
        val searchQuery = searchEditText.text.toString().trim()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment is SearchResultsFragment) {
            fragment.searchEventsByTitle(searchQuery)
        }
    }

    //Handle the results of the filter activity
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