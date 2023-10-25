package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import android.app.Activity

class MainActivity : AppCompatActivity() {
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var googleButton: ImageView

    private val rcSignIn = 1000
    private val startSignInForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the result if the sign-in is successful.
            val data = result.data
            onActivityResult(rcSignIn, Activity.RESULT_OK, data)
        } else {
            // Handle the result if the sign-in is not successful.
            // You can show an error message or take appropriate action here.
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SignUpUnderline = findViewById<TextView>(R.id.SignUpUnderline)
        //SignUpUnderline.setOnClickListener()

        googleButton = findViewById(R.id.google_btn)
        googleSignInClient = configureGoogleSignIn()

        googleButton.setOnClickListener { signIn() }

        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (lastSignedInAccount != null) {
            navigateToGoogleActivity()
        }
    }

    private fun configureGoogleSignIn(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startSignInForResult.launch(signInIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                navigateToGoogleActivity()
            } catch (e: ApiException) {
                showToast("Something went wrong")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToGoogleActivity() {
        finish()
        val intent = Intent(this, GoogleActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 1000
    }
}