package com.example.test

import android.net.Uri
import java.util.UUID

data class EventData(
        val id: UUID,
        val title: String,
        val description: String,
        val date: String,
        val time: String,
        val buildingName: String?,
        val address: String?,
        val userUploadedImageUrl: String?,
        val categories: List<String>
)