package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.EventAdapter
import com.example.test.EventData
import com.example.test.EventDbAccess
import com.google.android.material.snackbar.Snackbar

class SearchResultsFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var searchedButton: ImageButton
    private lateinit var searchEditText: EditText

    private var allEvents: List<EventData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        noResultsTextView = view.findViewById(R.id.noResultsTextView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventAdapter = EventAdapter(mutableListOf())
        recyclerView.adapter = eventAdapter

        // Fetch all data from the database initially
        allEvents = EventDbAccess(requireContext()).getEventDataFromDatabase()
        eventAdapter.updateData(allEvents)

        // Access searchedButton from activity_search.xml
        searchedButton = requireActivity().findViewById(R.id.searchedButton)
        searchedButton.setOnClickListener {
            performSearch()
        }

        // Access searchEditText from activity_search.xml
        searchEditText = requireActivity().findViewById(R.id.searchEditText)

        return view
    }

    private fun performSearch() {
        val searchQuery = searchEditText.text.toString().trim()
        searchEventsByTitle(searchQuery)
    }

    fun searchEventsByTitle(title: String) {
        val filteredEvents = allEvents.filter { it.title.contains(title, ignoreCase = true) }

        if (filteredEvents.isNotEmpty()) {
            showResults(filteredEvents)
        } else {
            showNoResults()
        }
    }

    private fun showResults(results: List<EventData>) {
        recyclerView.visibility = View.VISIBLE
        noResultsTextView.visibility = View.GONE
        eventAdapter.updateData(results)
    }

    private fun showNoResults() {
        recyclerView.visibility = View.GONE
        noResultsTextView.visibility = View.VISIBLE
    }
}