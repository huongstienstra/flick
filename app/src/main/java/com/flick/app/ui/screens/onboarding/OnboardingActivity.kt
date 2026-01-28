package com.flick.app.ui.screens.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flick.app.MainActivity
import com.flick.app.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnboardingScreen(modifier = Modifier.fillMaxSize(), onNextScreen = {
                startActivity(Intent(this, MainActivity::class.java))
            })
        }
    }
}