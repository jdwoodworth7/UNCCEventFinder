package com.example.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage


class RegisterActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContentView(R.layout.activity_register)
        val BackButton: ImageButton = findViewById(R.id.BackButton)
        BackButton.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        }


        val firstName: EditText = findViewById(R.id.editFirstName)
        val lastName: EditText = findViewById(R.id.editLastName)
        val email: EditText = findViewById(R.id.editEmail)
        val password: EditText = findViewById(R.id.editPassword)
        val signUpButton: Button = findViewById(R.id.SignUpButton)



        //val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserData")


        signUpButton.setOnClickListener {
            if (TextUtils.isEmpty(firstName.text.toString()) || TextUtils.isEmpty(lastName.text.toString()) || TextUtils.isEmpty(email.text.toString()) || TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(this, "Please fill out all of the prompts", Toast.LENGTH_SHORT).show()
            } else {
                val firstNameStr: String = firstName.text.toString()
                val lastNameStr: String = lastName.text.toString()
                val emailStr: String = email.text.toString()
                val passwordStr: String = password.text.toString()
                val blankTemp: String = ""
                val blankList = emptyList<String>()

                saveUserDataToFirestore(
                    emailStr ?: "",
                    firstNameStr ?: "",
                    lastNameStr ?: "",
                    passwordStr ?: "",
                    blankTemp ?: "",
                    blankList ?: emptyList(),
                )

                // Set a flag indicating that the user has just signed up
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("justSignedUp", true)
                // Add a key-value pair to indicate that the tutorial prompt hasn't been shown yet
                editor.putBoolean("showTutorialPrompt", true)
                editor.apply()


                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            }
        }
    }

    private fun saveUserDataToFirestore(
        email: String,
        firstname: String,
        lastname: String,
        password: String,
        status: String,
        friendids: List<String>
    ) {
        // Create a new event document in the "Users" collection
        val user = hashMapOf(
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,
            "password" to password,
            "status" to status,
            "friendids" to friendids
        )

        // Add the event to the "Users" collection
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")

                // Finish the current activity to remove it from the back stack
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding event document", e)
            }
    }
}