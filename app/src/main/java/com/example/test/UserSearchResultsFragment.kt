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
import com.example.test.UserAdapter
import com.example.test.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserSearchResultsFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var searchedButton: ImageButton
    private lateinit var searchEditText: EditText
    private val FILTER_REQUEST_CODE = 1
    private var allUsers: List<UserData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_search_results, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        noResultsTextView = view.findViewById(R.id.noResultsTextView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        userAdapter = UserAdapter(mutableListOf()).apply {
            setOnItemClickListener(object : UserAdapter.OnItemClickListener {
                override fun onItemClick(userData: UserData) {
                    openUserDetails(userData)
                }
            })
        }

        recyclerView.adapter = userAdapter

        // Fetch and display data from Firestore
        fetchUserDataFromFirestore()

        searchedButton = requireActivity().findViewById(R.id.searchedButton)
        searchedButton.setOnClickListener {
            performSearch()
        }

        searchEditText = requireActivity().findViewById(R.id.searchEditText)

        return view
    }

    private fun fetchUserDataFromFirestore() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot: QuerySnapshot = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .get()
                    .await()

                val users = querySnapshot.toObjects(UserData::class.java)
                allUsers = users // Update the allUsers list
                updateUI(users)
                println("Fetched ${users.size} users from Firestore")
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(users: List<UserData>) {
        requireActivity().runOnUiThread {
            if (users.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                userAdapter.updateData(users)
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
        searchUsersByName(searchQuery)
    }

    fun searchUsersByName(firstname: String) {
        val filteredUsers = allUsers.filter { it.firstname.contains(firstname, ignoreCase = true) }

        if (filteredUsers.isNotEmpty()) {
            showResults(filteredUsers)
        } else {
            showNoResults()
        }
    }

    private fun showResults(results: List<UserData>) {
        println("Showing ${results.size} results")
        recyclerView.visibility = View.VISIBLE
        noResultsTextView.visibility = View.GONE
        userAdapter.updateData(results)
    }

    // The rest of your code remains unchanged

    private fun openUserDetails(userData: UserData) {
        // Replace with your logic to open the user details activity
        // For example:
        // val intent = Intent(requireContext(), UserDetailsActivity::class.java)
        // intent.putExtra("user", userData)
        // startActivity(intent)
    }
}