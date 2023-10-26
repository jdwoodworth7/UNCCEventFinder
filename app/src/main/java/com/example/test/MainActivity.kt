package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.graphics.Paint
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SignUpUnderline = findViewById<TextView>(R.id.SignUpUnderline)
        val EmailButton = findViewById<Button>(R.id.EmailButton)
        SignUpUnderline.setOnClickListener{
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        EmailButton.setOnClickListener{
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

    }
}