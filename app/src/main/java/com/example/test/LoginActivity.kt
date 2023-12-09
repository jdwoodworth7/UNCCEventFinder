package com.example.test

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    var allUsers: List<UserData> = mutableListOf()
    private var authorId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login)
        val BackButton: ImageButton = findViewById(R.id.backButton)
        val loginbutton: Button = findViewById(R.id.logInButton)
        BackButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        val email: EditText = findViewById(R.id.editEmail)
        val password: EditText = findViewById(R.id.editPassword)
        val signUpUnderline2: TextView = findViewById(R.id.SignUpUnderline2)

        signUpUnderline2.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        loginbutton.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString()) || TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(this, "Please fill out all of the provided prompts", Toast.LENGTH_SHORT).show()
            } else {
                // Query the entered email to see if it already is being used
                val query = FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("email", email.text.toString())
                    .whereEqualTo("password", password.text.toString())
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            Toast.makeText(this, "There is no account associated with that email and password combination", Toast.LENGTH_SHORT).show()
                        } else {
                            // Assuming there's only one document for the given email and password
                            val userDocument = querySnapshot.documents[0]

                            // Access additional fields from the user's document
                            authorId = userDocument.id
                            val username = userDocument.getString("firstname")
                            val email = userDocument.getString("email")
                            val isModerator = userDocument.getBoolean("isModerator")

                            // Saying Welcome firstname
                            Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()

                            // Save authorId to SharedPreferences
                            saveAuthorIdToSharedPreferences(authorId, username, email, isModerator!!)

                            // Proceed to MapsActivity
                            val intent = Intent(this@LoginActivity, MapsActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
        }

    }

    // Save authorId to SharedPreferences
    private fun saveAuthorIdToSharedPreferences(authorId: String?, username: String?, email: String?, isModerator: Boolean) {
        val sharedPreferences = this.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("authorId", authorId)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putBoolean("isModerator", isModerator)
        editor.apply()
    }

}