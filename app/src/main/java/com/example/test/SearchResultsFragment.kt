package com.example.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private val FILTER_REQUEST_CODE = 1
    private var allEvents: List<EventData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        noResultsTextView = view.findViewById(R.id.noResultsTextView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        eventAdapter = EventAdapter(mutableListOf()).apply {
            setOnItemClickListener(object : EventAdapter.OnItemClickListener {
                override fun onItemClick(eventData: EventData) {
                    openEventDetails(eventData)
                }
            })
        }

        recyclerView.adapter = eventAdapter

        allEvents = EventDbAccess(requireContext()).getEventDataFromDatabase()
        eventAdapter.updateData(allEvents)

        searchedButton = requireActivity().findViewById(R.id.searchedButton)
        searchedButton.setOnClickListener {
            performSearch()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val filterData =
                data?.getParcelableExtra<FilterData>("filterData") ?: FilterData(
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            Log.d("SearchResultsFragment", "Received Filter Data: $filterData")

            applyFilter(filterData)
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

    fun applyFilter(filterData: FilterData) {
        val filteredEvents = allEvents.filter { event ->
            (event.categories.contains("Academic") && filterData.academic) ||
                    (event.categories.contains("Social") && filterData.social) ||
                    (event.categories.contains("Clubs/Organizations") && filterData.clubsOrg) ||
                    (event.categories.contains("Workshops/Seminars") && filterData.workshops) ||
                    (event.categories.contains("Volunteering") && filterData.volunteering) ||
                    (event.categories.contains("Students Only") && filterData.studentsOnly)
        }

        if (filteredEvents.isNotEmpty()) {
            showResults(filteredEvents)
        } else {
            showNoResults()
        }
    }

    // Open the event details activity
    private fun openEventDetails(eventData: EventData) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("event", eventData)
        startActivity(intent)
    }
}