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
    private var step4Button: Button? = null
    private var step5Button: Button? = null
    private var step6Button: Button? = null
    private var step7Button: Button? = null
    private var step8Button: Button? = null
    private var step9Button: Button? = null
    private var step10Button: Button? = null
    private var step11Button: Button? = null
    private var step12Button: Button? = null
    private var step13Button: Button? = null
    private var tutorialImageView: ImageView? = null
    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_layout)

        // Initialize your buttons, image view, and set initial visibility
        step1Button = findViewById<Button>(R.id.step1Button)
        step2Button = findViewById<Button>(R.id.step2Button)
        step3Button = findViewById<Button>(R.id.step3Button)
        step4Button = findViewById<Button>(R.id.step4Button)
        step5Button = findViewById<Button>(R.id.step5Button)
        step6Button = findViewById<Button>(R.id.step6Button)
        step7Button = findViewById<Button>(R.id.step7Button)
        step8Button = findViewById<Button>(R.id.step8Button)
        step9Button = findViewById<Button>(R.id.step9Button)
        step10Button = findViewById<Button>(R.id.step10Button)
        step11Button = findViewById<Button>(R.id.step11Button)
        step12Button = findViewById<Button>(R.id.step12Button)
        step13Button = findViewById<Button>(R.id.step13Button)
        tutorialImageView = findViewById<ImageView>(R.id.tutorialImageView)
        updateVisibility()

        // Set click listeners using the safe call operator
        step1Button?.setOnClickListener(this)
        step2Button?.setOnClickListener(this)
        step3Button?.setOnClickListener(this)
        step4Button?.setOnClickListener(this)
        step5Button?.setOnClickListener(this)
        step6Button?.setOnClickListener(this)
        step7Button?.setOnClickListener(this)
        step8Button?.setOnClickListener(this)
        step9Button?.setOnClickListener(this)
        step10Button?.setOnClickListener(this)
        step11Button?.setOnClickListener(this)
        step12Button?.setOnClickListener(this)
        step13Button?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        // Handle button clicks
        currentStep++
        if (currentStep <= 13) {
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

        findViewById<FrameLayout>(R.id.step4FrameLayout)?.visibility =
            if (currentStep == 4) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step5FrameLayout)?.visibility =
            if (currentStep == 5) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step6FrameLayout)?.visibility =
            if (currentStep == 6) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step7FrameLayout)?.visibility =
            if (currentStep == 7) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step8FrameLayout)?.visibility =
            if (currentStep == 8) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step9FrameLayout)?.visibility =
            if (currentStep == 9) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step10FrameLayout)?.visibility =
            if (currentStep == 10) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step11FrameLayout)?.visibility =
            if (currentStep == 11) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step12FrameLayout)?.visibility =
            if (currentStep == 12) View.VISIBLE else View.INVISIBLE

        findViewById<FrameLayout>(R.id.step13FrameLayout)?.visibility =
            if (currentStep == 13) View.VISIBLE else View.INVISIBLE

        // Update buttons visibility based on the current step
        step1Button?.visibility = if (currentStep == 1) View.VISIBLE else View.INVISIBLE
        step2Button?.visibility = if (currentStep == 2) View.VISIBLE else View.INVISIBLE
        step3Button?.visibility = if (currentStep == 3) View.VISIBLE else View.INVISIBLE
        step4Button?.visibility = if (currentStep == 4) View.VISIBLE else View.INVISIBLE
        step5Button?.visibility = if (currentStep == 5) View.VISIBLE else View.INVISIBLE
        step6Button?.visibility = if (currentStep == 6) View.VISIBLE else View.INVISIBLE
        step7Button?.visibility = if (currentStep == 7) View.VISIBLE else View.INVISIBLE
        step8Button?.visibility = if (currentStep == 8) View.VISIBLE else View.INVISIBLE
        step9Button?.visibility = if (currentStep == 9) View.VISIBLE else View.INVISIBLE
        step10Button?.visibility = if (currentStep == 10) View.VISIBLE else View.INVISIBLE
        step11Button?.visibility = if (currentStep == 11) View.VISIBLE else View.INVISIBLE
        step12Button?.visibility = if (currentStep == 12) View.VISIBLE else View.INVISIBLE
        step13Button?.visibility = if (currentStep == 13) View.VISIBLE else View.INVISIBLE
    }

    private fun updateImage() {
        // Update the tutorial image based on the current step
        tutorialImageView?.setImageResource(
            when (currentStep) {
                1 -> R.drawable.tutorial_step_1
                2 -> R.drawable.tutorial_step_2
                3 -> R.drawable.tutorial_step_3
                4 -> R.drawable.tutorial_step_4
                5 -> R.drawable.tutorial_step_5
                6 -> R.drawable.tutorial_step_6
                7 -> R.drawable.tutorial_step_7
                8 -> R.drawable.tutorial_step_8
                9 -> R.drawable.tutorial_step_9
                10 -> R.drawable.tutorial_step_10
                11 -> R.drawable.tutorial_step_11
                12 -> R.drawable.tutorial_step_12
                13 -> R.drawable.tutorial_step_13
                else -> R.drawable.baseline_image_24 // Change to a default image or handle accordingly
            }
        )
    }
}