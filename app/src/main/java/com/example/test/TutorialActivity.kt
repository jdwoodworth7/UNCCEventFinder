package com.example.test

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class TutorialActivity : AppCompatActivity() {

    private val tutorialImages = arrayOf(
        R.drawable.tutorial_step_1,
        R.drawable.tutorial_step_2,
        R.drawable.tutorial_step_3
        // Add more tutorial steps as needed
    )

    private var currentStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_layout)

        val tutorialImageView: ImageView = findViewById(R.id.tutorialImageView)
        val nextButton: Button = findViewById(R.id.nextButton)

        // Initial setup
        tutorialImageView.setImageResource(tutorialImages[currentStep])

        nextButton.setOnClickListener {
            // Handle the next button click
            if (currentStep < tutorialImages.size - 1) {
                // If there are more steps, move to the next step
                currentStep++
                tutorialImageView.setImageResource(tutorialImages[currentStep])
            } else {
                // If it's the last step, finish the tutorial or take appropriate action
                finishTutorial()
            }
        }
    }

    private fun finishTutorial() {
        // Implement logic for finishing the tutorial
        // This might include saving a preference to indicate that the user has completed the tutorial
        finish()
    }
}