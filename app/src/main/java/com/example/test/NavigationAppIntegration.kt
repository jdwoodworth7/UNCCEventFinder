package com.example.test
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

class NavigationAppIntegration(private val context: Context) {

    //Opens and starts navigation on Google Map
    fun starNavigationToGoogleMap(latitude: Double, longitude: Double){
        val geoUri = "google.navigation:q=$latitude,$longitude"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
        intent.setPackage("com.google.android.apps.maps")

        if(intent.resolveActivity(context.packageManager) != null){
            context.startActivity(intent)
        }
        else{
            //if google map is not installed
            Log.e("Location Sender", "Google Maps is not installed")
        }
    }
}