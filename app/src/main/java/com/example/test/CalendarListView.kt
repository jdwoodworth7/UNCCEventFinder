package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CalendarListView : AppCompatActivity() {
    //Makes the listview use the correct xml format
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_list_view)
    }
}