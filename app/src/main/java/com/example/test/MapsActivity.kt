package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.example.test.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var initialBounds: LatLngBounds? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        //TODO: i don't know, fix your brain and think when you get it back
        setInitialCameraBounds()

        mMap.moveCamera(CameraUpdateFactory.newLatLng(campusCoordinates))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.5F))
        mMap.setMinZoomPreference(14.75F)

        mMap.setOnCameraMoveListener {
            setZoomCameraBounds()
        }
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
}