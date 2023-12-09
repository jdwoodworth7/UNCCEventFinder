package com.example.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class GoogleActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var signOutBtn: Button

    private var firestore = FirebaseStorageUtil.getFirebaseFireStoreInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        signOutBtn = findViewById(R.id.signout)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val firstname = acct.givenName
            val lastname = acct.familyName
            val personEmail = acct.email
            name.text = personName
            email.text = personEmail

            //fetches user by email and check if user exists in the db
            fetchUserByEmail(personEmail.toString()) { userData ->
                if (userData != null) { //if fetching user data had no errors
                    if (userData.id.isBlank()) { //if a user is found existing in the database

                        //create a new user on the database with google login info
                        createUser(personEmail.toString(), firstname.toString(), lastname.toString())
                    }
                } else { //fetching user data encountered error
                    Toast.makeText(
                        this,
                        "Error encountered fetching exising user email",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }

        signOutBtn.setOnClickListener { signOut() }

        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener { signOut() }

        val loginButton: Button = findViewById(R.id.logInButton)

        //user login
        loginButton.setOnClickListener {
            Toast.makeText(this,"Welcome, ${acct?.displayName}", Toast.LENGTH_SHORT) //display welcome message
            startActivity(Intent(this@GoogleActivity, MapsActivity::class.java)) //start MapsActivity
        }
    }

    //fetches and checks if user exists with email as a param
    private fun fetchUserByEmail(email: String, callback: (UserData?) -> Unit) {
        val userDataRef = firestore.collection("Users")
        val query = userDataRef.whereEqualTo("email", email)

        query.get().addOnSuccessListener { querySnapshot ->
            //if the query result is not empty
            if (!querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents[0]
                val userData = documentSnapshot.toObject(UserData::class.java)

                //saves the user data into the shared preference
                saveUserInfoToSharedPreferences(userData?.id, userData?.firstname, userData?.email, userData?.isModerator!!)

                callback(userData) //user is found
            } else {
                callback(UserData()) //no user was found. proceeds to storing new account
            }
        }.addOnFailureListener { exception ->
            Log.e("UserData FETCH", "Error fetching user data: $exception")
            callback(null)
        }
    }

    //creates a new user in the database, with google login values passes as a param
    private fun createUser(email: String, firstname: String, lastname: String) {
        val userRef = firestore.collection("Users")

        val user = hashMapOf(
            "id" to "",
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,
            "password" to "GOOGLE_LOGIN", //password is not needed for google login members
            "status" to "",
            "friendids" to "",
            "privacy" to "public" //default value
        )

        userRef
            .add(user)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                Log.d("Firestore", "DocumentSnapshot added with ID: $documentId")

                user["id"] = documentId //alters the hashmap value of key "id" to the document id

                //updates the database with updated hashmap
                userRef.document(documentId).set(user)
                    .addOnSuccessListener {
                        Log.d("Firestore", "DocumentSnapshot updated with ID: $documentId")
                        saveUserInfoToSharedPreferences(documentId, user[firstname], user[email], false)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error updating event document", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding event document", e)
            }

    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener { task ->
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    //saves user information to the shared preferences
    private fun saveUserInfoToSharedPreferences(userId: String?, username: String?, email: String?, isModerator: Boolean) {
        val sharedPreferences = this.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("authorId", userId)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putBoolean("isModerator", isModerator)
        editor.apply()
    }
}
