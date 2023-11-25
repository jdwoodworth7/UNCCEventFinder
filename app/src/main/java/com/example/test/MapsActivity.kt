package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.UUID


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var initialBounds: LatLngBounds? = null
    private lateinit var placesClient: PlacesClient
    private var markerSelected = false

//    private lateinit var selectedEvent: Event

    val MAPS_API_KEY = "AIzaSyBWL4qwmL_44-8UFds3yZqQH5IWk_OnCUw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //initialize and create client for Places API
        Places.initialize(applicationContext, MAPS_API_KEY)
        placesClient = Places.createClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get a reference to the menuButton
        val menuButton = findViewById<ImageView>(R.id.menuButton)

        // Set a click listener for the menuButton
        menuButton.setOnClickListener {
            // Start the MenuActivity when the menuButton is clicked
            val intent = Intent(this@MapsActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingInflatedId")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //initializes the point of camera focus by coordinates
        val campusCoordinates = LatLng(35.30856979486446, -80.73370233971224)
        setInitialCameraBounds()

        mMap.moveCamera(CameraUpdateFactory.newLatLng(campusCoordinates))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.5F))
        mMap.setMinZoomPreference(14.75F)

        mMap.setOnCameraMoveListener {
            setZoomCameraBounds()
        }

        fetchEventDataFireStore()

        //when marker is clicked
        mMap.setOnMarkerClickListener { clickedMarker ->
            markerSelected=true
            val eventId = clickedMarker.tag as String

            //if current marker's tag is not null
            if (eventId != null) {
                    fetchEventByFireStoreID(eventId) //Match clicked marker's event with event in db using id
                    { event ->
                        if(event != null){
                            //zooms the camera to the clicked marker position to a certain extent
                            val markerLatLng = clickedMarker.position
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLatLng, 17.5f)
                            mMap.animateCamera(cameraUpdate)

                            //inflates overlay layout
                            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val overlayView = inflater.inflate(R.layout.activity_mapstest, null)

                            //finds and stores views from overlay layout
                            val titleTextView: TextView = overlayView.findViewById(R.id.eventTitle)
                            val authorTextView: TextView = overlayView.findViewById(R.id.eventAuthor)
                            val addressTextView: TextView = overlayView.findViewById(R.id.eventAddress)
                            val imageView: ImageView = overlayView.findViewById(R.id.eventImage)
                            val moreDetailsButton: Button = overlayView.findViewById(R.id.btnMoreDetails)
                            val navigateButton: Button = overlayView.findViewById(R.id.btnNavOverlay)

                            //initialize the main view that encompasses both Google Maps and the layout
                            val rootView = findViewById<RelativeLayout>(R.id.mapContainer)

                            //assign parameter for the new layout
                            val params = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )

                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) //align to bottom of parent
                            overlayView.layoutParams = params

                            rootView.addView(overlayView)

                            titleTextView.text = event.title
                            //TODO: Add author column in DB
                            // authorTextView.text = event.author
                            addressTextView.text = event.address

                            //load event image
                            imageView.load(event.userUploadedImageUrl)

                            moreDetailsButton.setOnClickListener {
                                val intent = Intent(this@MapsActivity, DetailsActivity::class.java)

                                //put the Event object (parcelized) as an extra in the intent
                                intent.putExtra("event", event)

                                // Start the next activity
                                startActivity(intent)
                            }

                            navigateButton.setOnClickListener {
                                sendLocationNavigation(event)
                            }

                            //shows the marker title above the marker
                            clickedMarker.title = event.title
                            clickedMarker.showInfoWindow()

                            mMap.setOnMapClickListener {
                                if(markerSelected){
                                    markerSelected = false
                                    rootView.removeView(overlayView)
                                }
                            }
                        }
                    }
            } else {
                Log.e("Invalid Marker", "No valid event ID found for the clicked marker")
            }
            true//consumes the click event
        }
    }

    //locks camera movement to areas within campus
    private fun setInitialCameraBounds() {
        val minLatitude = 35.30
        val minLongitude = -80.745
        val maxLatitude = 35.315
        val maxLongitude = -80.720

        val southwest = LatLng(minLatitude, minLongitude)
        val northeast = LatLng(maxLatitude, maxLongitude)
        initialBounds = LatLngBounds(southwest, northeast)

        mMap.setLatLngBoundsForCameraTarget(initialBounds)
    }

    private fun setZoomCameraBounds() {
        //val currentCameraPosition = mMap.cameraPosition
        //val currentZoom = currentCameraPosition.zoom

        val minLatitude = initialBounds?.southwest?.latitude ?: 0.0
        val maxLatitude = initialBounds?.northeast?.latitude ?: 0.0
        val minLongitude = initialBounds?.southwest?.longitude ?: 0.0
        val maxLongitude = initialBounds?.northeast?.longitude ?: 0.0

        val newBounds = LatLngBounds(
            LatLng(minLatitude, minLongitude),
            LatLng(maxLatitude, maxLongitude)
        )

        mMap.setLatLngBoundsForCameraTarget(newBounds)
    }

    //Fetching all event data from the FireStore
    private fun fetchEventDataFireStore() {
        val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
        val eventCollectionRef = firestore.collection("Events")
        val eventList = mutableListOf<EventData>()

        eventCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    //eventId is not the id assigned to the data inside event (ID field),
                    //but a id that is assigned to the document which contains the event data
                    val eventId = document.id
                    val eventData = document.toObject(EventData::class.java)

                    eventList.add(eventData)

                    fetchLatLngFromAddress(eventData) { lat, lng ->
                        val latLng = LatLng(lat, lng)

                        //adds marker on given coordinate
                        //TODO: automate alternate marker locations so that events of the same address won't stack on top
                        //Suggestion: If multiple events occur at the same address, display titles of multiple events in list view
                        runOnUiThread {
                            val newMarker = mMap.addMarker(
                                MarkerOptions().position(latLng).title(eventData.title)
                            )
                            //assign event's unique id to the marker
                            newMarker?.tag = eventData.id
                        }
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "Reference Fetch Failure",
                    "Failed to fetch docuemnts from the collection reference: " + exception
                )
            }
    }

    //Fetching event from local DB by eventID
    private fun fetchEventByID(id: UUID): EventData? {
        val eventDbAccess = EventDbAccess(this)
        val eventList = eventDbAccess.getEventDataFromDatabase()

        for (event in eventList) {
            if (event.id == id.toString()) {
                return event
            }
        }
        return null
    }

    private fun fetchEventByFireStoreID(id: String, callback: (EventData?) -> Unit) {
        val firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()
        val eventRef = firestore.collection("Events").document(id)

        eventRef.get()
            .addOnSuccessListener { querySnapShot ->
                if (querySnapShot.exists()) {
                    val eventData = querySnapShot.toObject(EventData::class.java)
                    callback(eventData)
                } else{
                    Log.e("Event FetchById Request", "Following event does not exist")
                    callback(null)
                }
            }
            .addOnFailureListener{ exception ->
                Log.e("Event FetchById Request", "Event Fetch Failed: " + exception)
            }
    }

    //for future use, prediction address for better accuracy
    fun searchAddressByPrediction(event: EventData) {
        val address = event.address

        val addressLatLng = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        //AutoComplete Prediction SQL request to Places API
        val predictionRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery(address) // queries for current event's address
            .setSessionToken(AutocompleteSessionToken.newInstance())
            .build()

        //returns predicted address from Places API
        placesClient.findAutocompletePredictions(predictionRequest)
            .addOnSuccessListener { response ->
                if (response.autocompletePredictions.isNotEmpty()) {
                    val firstPrediction = response.autocompletePredictions[0]
                    val addressId = firstPrediction.placeId

                    val fetchPlaceRequest = FetchPlaceRequest.newInstance(addressId, addressLatLng)

                    /*  NOTE - use prediction.zzd for full address
                        use prediction.zze for street address only
                        use prediction.zzf for City, State, and Country* */

                    placesClient.fetchPlace(fetchPlaceRequest)
                        .addOnSuccessListener { response ->
                            val place = response.place
                            mMap.addMarker(
                                MarkerOptions().position(place.latLng).title(event.title)
                            )
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code")
                            }
                        }
                }
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("Place Prediction", "Place not found: " + exception.statusCode)
                }
            }

    }

    //queries latitude and longitude and sends value to NavigationAppIntegration
    private fun sendLocationNavigation(event: EventData){
        fetchLatLngFromAddress(event) {lat, lng ->
            val navigationAppIntegration = NavigationAppIntegration(this)
            navigationAppIntegration.starNavigationToGoogleMap(lat, lng)
        }
    }

    //Fetching Existing Event Data from DB
    private fun fetchEventData() {
        val eventDbAccess = EventDbAccess(this)
        val eventList = eventDbAccess.getEventDataFromDatabase()

        //iterates for each event inside eventList
        for (event in eventList) {
            //searches and fetches latitude and longitude from input address
            fetchLatLngFromAddress(event) { lat, lng ->
                val latLng = LatLng(lat, lng)

                //adds marker on given coordinate
                //TODO: automate alternate marker locations so that events of the same address won't stack on top
                //Suggestion: If multiple events occur at the same address, display titles of multiple events in list view
                runOnUiThread {
                    val newMarker = mMap.addMarker(
                        MarkerOptions().position(latLng).title(event.title)
                    )
                    //assign event's unique id to the marker
                    newMarker?.tag = event.id
                }
            }
        }
    }

}