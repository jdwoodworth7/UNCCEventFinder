package com.example.test

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ReportActivity : AppCompatActivity(){

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator_reported_users)  //sets the content view to main, parent view

        val listView = findViewById<ListView>(R.id.userListView) //defines list view to populate data into it

        runBlocking{
            val userList = fetchAllUsersWithReportCases() //fetches list of users with report cases

            if(userList.isNotEmpty()){
                //passes the datasource into the adapter
                val adapter = ReportedUserListAdapter(this@ReportActivity,userList)
                listView.adapter = adapter //gets the view based on adapter options
            }
        }

    }

    //fetches mutable list of UserData with any report cases
    suspend fun fetchAllUsersWithReportCases () : MutableList<UserData> {
        return withContext(Dispatchers.IO) {
            val userCollectionRef = firestore.collection("Users")
            val query = userCollectionRef.whereGreaterThan("reportCases", 0)

            try {
                val querySnapShot = Tasks.await(query.get())
                val users : MutableList<UserData> = querySnapShot.toObjects(UserData::class.java)
                users
            } catch(exception: Exception) {
                Log.e("Users Data Fetch Request" , "Users with Report Cases Fetch failed: $exception")
                mutableListOf()
            }
        }
    }

}