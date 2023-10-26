package com.example.test

data class EventData(
    val title: String,
    val description: String,
    val dateAndTime: String,
    val buildingName: String?,
    val address: String?,
    val categories: List<String>
)
