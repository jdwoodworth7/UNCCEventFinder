package com.example.test

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CreateEventActivity : AppCompatActivity() {

    private lateinit var selectImageButton: Button
    private lateinit var storageReference: StorageReference
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var editBuildingName: EditText
    private lateinit var editAddress: EditText
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize Firebase Storage reference for images
        storageReference = FirebaseStorage.getInstance().getReference("event_images")

        // Initialize the EditText and uploadButton variables
        editTitle = findViewById(R.id.titleEditText)
        editDescription = findViewById(R.id.descriptionEditText)
        editBuildingName = findViewById(R.id.locationEditText)
        editAddress = findViewById(R.id.addressEditText)
        selectImageButton = findViewById(R.id.uploadButton)

        //Upload Image Button
        selectImageButton.setOnClickListener {
            // Open the image picker
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }

        val editTitle = findViewById<EditText>(R.id.titleEditText)
        val editDescription = findViewById<EditText>(R.id.descriptionEditText)
        val editBuildingName = findViewById<EditText>(R.id.locationEditText)
        val editAddress = findViewById<EditText>(R.id.addressEditText)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val menuButton = findViewById<ImageView>(R.id.menuButton)
        val mapIcon = findViewById<ImageView>(R.id.mapIcon)
        val descriptionCounter = findViewById<TextView>(R.id.descriptionCounter)
        val locationCounter = findViewById<TextView>(R.id.locationCounter)

        // Add a TextWatcher to update the description counter
        editDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the description counter
                val currentCount = s?.length ?: 0
                descriptionCounter.text = "$currentCount/200"
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing here
            }
        })

        // Add a TextWatcher to update the Building Name counter
        editBuildingName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the location counter
                val currentCount = s?.length ?: 0
                locationCounter.text = "$currentCount/80"
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing here
            }
        })

        continueButton.setOnClickListener {
            // Get the entered data
            val title = editTitle.text.toString().trim()
            val description = editDescription.text.toString().trim()
            val buildingName = editBuildingName.text.toString().trim()
            val address = editAddress.text.toString().trim()

            // Check if any of the required fields are empty
            if (title.isEmpty() || description.isEmpty() || buildingName.isEmpty() || address.isEmpty()) {
                // Display a message or toast indicating that all required fields must be filled
                Toast.makeText(this@CreateEventActivity, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            } else if (imageUri == null) {
                // Display a message or toast indicating that the user must upload an image
                Toast.makeText(this@CreateEventActivity, "Please upload an image", Toast.LENGTH_SHORT).show()
            } else {
                // Continue with your logic for a selected image
                val intent = Intent(this@CreateEventActivity, CreateEventSessionsActivity::class.java)

                // Continue with your other data passing logic...
                // Pass the image URI to the next activity
                intent.putExtra("imageUri", imageUri.toString())
                intent.putExtra("title", title)
                intent.putExtra("description", description)
                intent.putExtra("buildingName", buildingName)
                intent.putExtra("address", address)

                // Start the next activity
                startActivity(intent)
            }
        }

        cancelButton.setOnClickListener {
            // If the user clicks the "Cancel" button, open the menu
            val intent = Intent(this@CreateEventActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        menuButton.setOnClickListener {
            // Open the menu activity when the menu button is clicked
            val intent = Intent(this@CreateEventActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        mapIcon.setOnClickListener {
            // Open the map activity when the map button is clicked
            val intent = Intent(this@CreateEventActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.data != null) {
                // Get the selected image URI directly
                imageUri = data.data!!
            }
        }
    }
}