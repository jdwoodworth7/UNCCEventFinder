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

class LoginActivity : AppCompatActivity() {
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
                if(TextUtils.equals(email.text.toString(), "admin@gmail.com") && TextUtils.equals(password.text.toString(), "admin")){
                    startActivity(Intent(this@LoginActivity, MapsActivity::class.java))
                }else{
                    Toast.makeText(this, "Please enter a valid account email and password", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}