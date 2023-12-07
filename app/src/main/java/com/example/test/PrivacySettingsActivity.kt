package com.example.test

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PrivacySettingsActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    private lateinit var showEventsSwitch: CheckBox

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)

        showEventsSwitch = findViewById(R.id.checkBoxEvents)

        userId = getAuthorIdFromSharedPreferences() // gets userID from SharedPreference

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        val saveButton: Button = findViewById(R.id.btnSave)
        // Load current privacy settings
        loadPrivacySettings()

        // Handle save button click
        saveButton.setOnClickListener {
            savePrivacySettings()
            finish()
        }
    }

    private fun loadPrivacySettings() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).child("privacySettings")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val showEvents = snapshot.child("showEvents").getValue(Boolean::class.java) ?: true
                        showEventsSwitch.isChecked = showEvents
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    private fun savePrivacySettings() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val showEvents = showEventsSwitch.isChecked
            database.child("users").child(userId).child("privacySettings")
                .child("showEvents").setValue(showEvents)
        }
    }

    //gets the userId initialized during login
    private fun getAuthorIdFromSharedPreferences(): String {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val authorId = sharedPreferences.getString("authorId", null)
        return authorId.toString()
    }
}
