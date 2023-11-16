package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val SignUpUnderline = findViewById<TextView>(R.id.SignUpUnderline)
        val EmailButton = findViewById<Button>(R.id.EmailButton)
        SignUpUnderline.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        EmailButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        googleBtn = findViewById(R.id.google_btn)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            navigateToGoogleActivity()
        }

        googleBtn.setOnClickListener { signIn() }
    }

        private fun signIn() {
            val signInIntent = gsc.signInIntent
            startActivityForResult(signInIntent, 1000)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1000) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    task.getResult(ApiException::class.java)
                    navigateToGoogleActivity()
                } catch (e: ApiException) {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        private fun navigateToGoogleActivity() {
            finish()
            val intent = Intent(this, GoogleActivity::class.java)
            startActivity(intent)
        }
}
