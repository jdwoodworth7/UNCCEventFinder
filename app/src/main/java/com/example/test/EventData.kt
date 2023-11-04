package com.example.test

import android.net.Uri

data class EventData(
    val title: String,
    val description: String,
    val dateAndTime: String,
    val buildingName: String?,
    val address: String?,
    val userUploadedImageUri: Uri,
    val categories: List<String>
)
