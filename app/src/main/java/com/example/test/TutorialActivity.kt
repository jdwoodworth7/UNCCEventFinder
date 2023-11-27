package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity : AppCompatActivity(), View.OnClickListener {
    private var step1Button: Button? = null
    private var step2Button: Button? = null
    private var step3Button: Button? = null
    private var tutorialImageView: ImageView? = null
    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_layout)

        // Initialize your buttons, image view, and set initial visibility
        step1Button = findViewById<Button>(R.id.step1Button)
        step2Button = findViewById<Button>(R.id.step2Button)
        step3Button = findViewById<Button>(R.id.step3Button)
        tutorialImageView = findViewById<ImageView>(R.id.tutorialImageView)
        updateVisibility()

        // Set click listeners using the safe call operator
        step1Button?.setOnClickListener(this)
        step2Button?.setOnClickListener(this)
        step3Button?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        // Handle button clicks
        currentStep++
        if (currentStep <= 3) {
            updateVisibility()
            updateImage()
        } else {
            // If it's the last step, go back to MainActivity
            val intent = Intent(this@TutorialActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateVisibility() {
        // Update visibility based on the current step
        findViewById<FrameLayout>(R.id.step1FrameLayout)?.visibility =
            if (currentStep == 1) View.VISIBLE else View.INVISIBLE
        findViewById<FrameLayout>(R.id.step2FrameLayout)?.visibility =
            if (currentStep == 2) View.VISIBLE else View.INVISIBLE
        findViewById<FrameLayout>(R.id.step3FrameLayout)?.visibility =
            if (currentStep == 3) View.VISIBLE else View.INVISIBLE

        // Update buttons visibility based on the current step
        step1Button?.visibility = if (currentStep == 1) View.VISIBLE else View.INVISIBLE
        step2Button?.visibility = if (currentStep == 2) View.VISIBLE else View.INVISIBLE
        step3Button?.visibility = if (currentStep == 3) View.VISIBLE else View.INVISIBLE
    }

    private fun updateImage() {
        // Update the tutorial image based on the current step
        tutorialImageView?.setImageResource(
            when (currentStep) {
                1 -> R.drawable.tutorial_step_1
                2 -> R.drawable.tutorial_step_2
                3 -> R.drawable.tutorial_step_3
                else -> R.drawable.baseline_image_24 // Change to a default image or handle accordingly
            }
        )
    }
}