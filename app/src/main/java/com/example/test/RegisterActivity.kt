package com.example.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        signUpButton.setOnClickListener {
            if (TextUtils.isEmpty(firstName.text.toString()) || TextUtils.isEmpty(lastName.text.toString()) || TextUtils.isEmpty(email.text.toString()) || TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(this, "Please fill out all of the prompts", Toast.LENGTH_SHORT).show()
            } else {
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
}