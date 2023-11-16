package com.example.test

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

val API_KEY = "AIzaSyBWL4qwmL_44-8UFds3yZqQH5IWk_OnCUw"

//fetches Latitude and Longitude from Json response string using Gson library
fun fetchLatLngFromAddress(event: EventData, onLatLngReceived: (Double, Double) -> Unit) {
    getLocationByAddress(event.address,
        onResponse = { response ->

            val jsonString = response.body?.string()

            //checks if response string is not empty
            if (jsonString != null) {
                val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)

                val resultsArray = jsonObject.getAsJsonArray("results")

                if (resultsArray.size() > 0) {
                    val locationObject = resultsArray[0].asJsonObject
                        .getAsJsonObject("geometry")
                        .getAsJsonObject("location")

                    val lat = locationObject.getAsJsonPrimitive("lat").asDouble
                    val lng = locationObject.getAsJsonPrimitive("lng").asDouble

                    //returns Latitude and Longitude
                    onLatLngReceived(lat, lng)

                } else {
                    //if json string is not empty but results has no value
                    Log.e("Response Body Results Array", "Results are empty")
                }
            } else {
                //if response json string is empty
                Log.e("Response Body", "Response Body is Empty")
            }
        },
        onFailure = { exception ->
            exception.printStackTrace()
        }
    )
}

//HTTP Get request to Places API for address search.
fun getLocationByAddress(
    address: String?,
    onResponse: (Response) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val client = OkHttpClient()
    val url = HttpUrl.Builder()
        .scheme("https")
        .host("maps.googleapis.com")
        .addPathSegment("maps")
        .addPathSegment("api")
        .addPathSegment("geocode")
        .addPathSegment("json")
        .addQueryParameter("address", address)
        .addQueryParameter("key", API_KEY)
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e)
        }

        override fun onResponse(call: Call, response: Response) {
            onResponse(response)
        }

    })
}