package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myfriends_event)

        val addFriendsButton: Button = findViewById(R.id.AddFriendsButton)

        addFriendsButton.setOnClickListener {
            val intent = Intent(this, SearchUsersActivity::class.java)
            startActivity(intent)
        }
        // Find the menu button and set a click listener
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@AddFriendsActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}