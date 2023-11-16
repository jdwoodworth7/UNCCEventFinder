package com.example.test

import android.net.Uri
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class EventData(
        val id: UUID,
        val title: String,
        val description: String,
        val date: LocalDate,
        val time: LocalTime,
        val buildingName: String?,
        val address: String?,
        val userUploadedImageUrl: String?,
        val categories: List<String>
)
