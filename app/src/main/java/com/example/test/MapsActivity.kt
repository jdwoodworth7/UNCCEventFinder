package com.example.test

import android.content.Intent
import android.gesture.Prediction
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.example.test.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var initialBounds: LatLngBounds? = null
    private lateinit var placesClient: PlacesClient

    val MAPS_API_KEY = "AIzaSyBWL4qwmL_44-8UFds3yZqQH5IWk_OnCUw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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

        fetchEventData()

        // Add a marker
        //mMap.addMarker(MarkerOptions().position(campusCoordinates).title("Marker Name"))
    }

    //locks camera movement to areas within campus
    fun setInitialCameraBounds() {
        val minLatitude = 35.30
        val minLongitude = -80.745
        val maxLatitude = 35.315
        val maxLongitude = -80.720

        val southwest = LatLng(minLatitude, minLongitude)
        val northeast = LatLng(maxLatitude, maxLongitude)
        initialBounds = LatLngBounds(southwest, northeast)

        mMap.setLatLngBoundsForCameraTarget(initialBounds)
    }

    fun setZoomCameraBounds() {
        val currentCameraPosition = mMap.cameraPosition
        val currentZoom = currentCameraPosition.zoom

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

    //Fetching Existing Event Data (TEMP: Local)
    fun fetchEventData() {
        val eventDbAccess = EventDbAccess(this)
        val eventList = eventDbAccess.getEventDataFromDatabase()

        //iterates for each event inside eventList
        for (event in eventList) {
            searchAddress(event)
        }
    }

    fun searchAddress(event: EventData) {
        val address = event.address

        val addressLatLng = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        //SQL request to Places API
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
}