package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PrivacySettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = getAuthorIdFromSharedPreferences() // gets userID from SharedPreference

        // Load current privacy settings
        loadPrivacySettings(userId)

        setContentView(R.layout.activity_privacy_settings)

        val saveButton: Button = findViewById(R.id.btnSave)

        // Handle save button click
        saveButton.setOnClickListener {
            savePrivacySettings(userId)
            finish()
        }

        // Find the menu button and set a click listener
        val menuButton: ImageView = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // Open the MenuActivity when the menu button is clicked
            val intent = Intent(this@PrivacySettingsActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        // Find the map icon and set a click listener
        val mapIcon: ImageView = findViewById(R.id.mapIcon)
        mapIcon.setOnClickListener {
            // Open the MapActivity when the map icon is clicked
            val intent = Intent(this@PrivacySettingsActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPrivacySettings(userId: String) {
        val userCollection = FirebaseFirestore.getInstance().collection("Users")

        userCollection.whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val userData = UserData(documentSnapshot)
                    val checkBoxEvents: CheckBox = findViewById(R.id.checkBoxEvents)

                    checkBoxEvents.isChecked = userData.privacy == "public"
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(this, "Failed to load privacy settings", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePrivacySettings(userId: String) {
        val checkBoxEvents: CheckBox = findViewById(R.id.checkBoxEvents)
        val newPrivacy = if (checkBoxEvents.isChecked) "public" else "private"

        val userCollection = FirebaseFirestore.getInstance().collection("Users")

        userCollection.whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val userRef = userCollection.document(documentSnapshot.id)

                    // Update the privacy setting in Firestore
                    userRef.update("privacy", newPrivacy)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Privacy settings saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                            Toast.makeText(this, "Failed to save privacy settings", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(this, "Failed to save privacy settings", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getAuthorIdFromSharedPreferences(): String {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val authorId = sharedPreferences.getString("authorId", null)
        return authorId.toString()
    }
}
