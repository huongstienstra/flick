package com.shinlee.showplus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.IntRect
import androidx.lifecycle.lifecycleScope
import com.shinlee.showplus.ui.screens.permission.PermissionActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        // Start a coroutine to delay and move to the next activity
        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, PermissionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}