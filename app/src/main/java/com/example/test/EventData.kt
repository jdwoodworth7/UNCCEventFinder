package com.example.test

import android.net.Uri

data class EventData(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val buildingName: String?,
    val address: String?,
    val userUploadedImageUrl: String?,
    val categories: List<String>
)
