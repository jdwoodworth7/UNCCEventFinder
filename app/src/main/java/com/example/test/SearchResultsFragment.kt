package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class SearchResultsFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventAdapter = EventAdapter(mutableListOf())
        recyclerView.adapter = eventAdapter

        // Fetch data from the database and update the adapter
        val eventList = EventDbAccess(requireContext()).getEventDataFromDatabase()
        eventAdapter.updateData(eventList)

        return view
    }
}