package com.example.test

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

        loginbutton.setOnClickListener{
            if(TextUtils.isEmpty(email.text.toString()) || TextUtils.isEmpty(password.text.toString())){
                Toast.makeText(this, "Please fill out all of the provided prompts", Toast.LENGTH_SHORT).show()
            } else {
                //Query the entered email to see if it already is being used
                val query = FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("email", email.text.toString())
                    .whereEqualTo("password", password.text.toString())
                    .get()
                    .addOnSuccessListener {
                        if(it.isEmpty) {
                            Toast.makeText(this, "There is no account associated with that email and password combination", Toast.LENGTH_SHORT).show()
                        } else {
                            startActivity(Intent(this@LoginActivity, MapsActivity::class.java))
                        }
                    }
            }

        }

    }
}