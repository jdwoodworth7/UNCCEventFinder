package com.example.test

import android.os.Bundle
import android.view.View  // Add this import
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Check if the fragment container exists before adding the fragment
        if (findViewById<View>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }

            val searchResultsFragment = SearchResultsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, searchResultsFragment)
                .commit()
        }
    }
}