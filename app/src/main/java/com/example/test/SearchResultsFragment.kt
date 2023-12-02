package com.example.test

import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

        // Fetch and display data from Firestore
        fetchEventDataFromFirestore()

        searchedButton = requireActivity().findViewById(R.id.searchedButton)
        searchedButton.setOnClickListener {
            performSearch()
        }

        searchEditText = requireActivity().findViewById(R.id.searchEditText)

        return view
    }

    private fun fetchEventDataFromFirestore() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot: QuerySnapshot = FirebaseFirestore.getInstance()
                    .collection("Events")
                    .get()
                    .await()

                val events = querySnapshot.toObjects(EventData::class.java)
                allEvents = events // Update the allEvents list
                updateUI(events)
                println("Fetched ${events.size} events from Firestore")
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(events: List<EventData>) {
        requireActivity().runOnUiThread {
            if (events.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                eventAdapter.updateData(events)
            } else {
                showNoResults()
            }
        }
    }

    private fun showNoResults() {
        println("No results found")
        recyclerView.visibility = View.GONE
        noResultsTextView.visibility = View.VISIBLE
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
        println("Showing ${results.size} results")
        recyclerView.visibility = View.VISIBLE
        noResultsTextView.visibility = View.GONE
        eventAdapter.updateData(results)
    }

    fun applyFilter(filterData: FilterData) {
        println("Before filtering: ${allEvents.size} events")

        val filteredEvents = allEvents.filter { event ->
            val matches = event.categories.any { category ->
                when {
                    (category.equals("academic", ignoreCase = true) && filterData.category_academic) ||
                            (category.equals("social", ignoreCase = true) && filterData.category_social) ||
                            (category.equals("sports", ignoreCase = true) && filterData.category_sports) ||
                            (category.equals("clubs/organizations", ignoreCase = true) && filterData.category_clubs) ||
                            (category.equals("workshops/seminars", ignoreCase = true) && filterData.category_workshops) ||
                            (category.equals("volunteering", ignoreCase = true) && filterData.category_volunteering) ||
                            (category.equals("students only", ignoreCase = true) && filterData.category_students_only) -> true
                    else -> false
                }
            }

            if (!matches) {
                println("Event ${event.title} did not pass the filter. Categories: ${event.categories}")
            }

            matches
        }

        println("After filtering: ${filteredEvents.size} events")

        if (filteredEvents.isNotEmpty()) {
            showResults(filteredEvents)
        } else {
            showNoResults()
        }

        println("Filtered events: $filteredEvents")
    }

    private fun openEventDetails(eventData: EventData) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("event", eventData)
        startActivity(intent)
    }
}