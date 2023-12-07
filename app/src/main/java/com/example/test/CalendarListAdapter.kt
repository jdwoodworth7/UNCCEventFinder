package com.example.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import coil.load
import com.example.test.EventDbAccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class CalendarListAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<UserEventsData>
) : ArrayAdapter<UserEventsData>(context, R.layout.activity_calendar_list_view, arrayList) {

    private val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
    private lateinit var userId: String

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Sets up the listview
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_calendar_list_view, null)

        //Finds the listview parts
        val icon: ImageView = view.findViewById(R.id.calendarListIcon)
        val title: TextView = view.findViewById(R.id.calendarListTitle)
        val time: TextView = view.findViewById(R.id.calendarListTime)
        val navButton: Button = view.findViewById(R.id.buttonNavigate)
        val removeButton: Button = view.findViewById(R.id.buttonRemove)

        runBlocking {
            val eventData = fetchEventByEventId(arrayList[position].eventId)
            val imageUrl: String? = eventData?.imageUri      //Used for event image

            //Uses coil to load the image url from a file pathway string
            icon.load(imageUrl)
            icon.visibility = View.VISIBLE
            title.text = eventData?.title
            time.text = eventData?.time

            //onClickListener for NavigationButton
            navButton.setOnClickListener {
                handleNavigationButtonClick(position, eventData)
            }

            removeButton.setOnClickListener {
                handleRemovalButtonClick(position)
            }
        }

        return view
    }

    private suspend fun fetchEventByEventId(eventId: String) :EventData? {
        return withContext(Dispatchers.IO) {
            val eventsRef = firestore.collection("Events").document(eventId)

            try{
                val documentSnapshot = eventsRef.get().await()
                if(documentSnapshot.exists()){
                    val eventData = documentSnapshot.toObject(EventData::class.java)
                    eventData
                }
                else{
                    Log.e("EventData","No Event Data exists. Fetch failed:")
                    null
                }
            } catch (exception: Exception){
                Log.e("EventData","fetch Event Data by Id failed: $exception")
                null
            }
        }
    }

    private fun handleNavigationButtonClick(position: Int, eventData: EventData?) {
        //queries latitude and longitude and sends value to NavigationAppIntegration
        fetchLatLngFromAddress(eventData!!) { lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(context)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }

    //TODO: make remove functional to the Firebase
    private fun handleRemovalButtonClick(position: Int) {
        //focuses on the selected event among events in the list (same date)
        val userEvent = arrayList[position]

        val userEventRef = firestore.collection("UserEvents").document(userEvent.id)

        userEventRef.delete()
            .addOnSuccessListener { documentSnapshot ->
                Log.d("Firestore DELETE", "Document successfully deleted!")

            }
            .addOnFailureListener {exception ->
                Log.e("Firestore DELETE", "Error in deleting the document", exception)
            }

        val intent = Intent(context, CalendarViewActivity::class.java)
        context.startActivity(intent)
    }

    private fun getUserIdFromSharedPreferences(): String {
        val sharedPreferences = context.getSharedPreferences(
            "MyPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val userId = sharedPreferences.getString("authorId", null)
        return userId.toString()
    }
}