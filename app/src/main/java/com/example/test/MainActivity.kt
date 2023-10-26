package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.graphics.Paint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SignUpUnderline = findViewById<TextView>(R.id.SignUpUnderline)
//        SignUpUnderline.setOnClickListener()
    }
}