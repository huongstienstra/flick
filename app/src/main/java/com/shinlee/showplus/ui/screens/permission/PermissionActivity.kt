package com.shinlee.showplus.ui.screens.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionActivity : AppCompatActivity(), PermissionCheckEvent {

    // Define the permissions you need
    private val requiredPermissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { // In here, we can call composables!
            MaterialTheme {
                PermissionScreen(this)
            }
        }
    }

    private fun checkPermissions() {
        val permissionsNeeded = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            // Request the permissions
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            // All permissions are granted, you can proceed with your functionality
            proceedWithFunctionality()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Toast.makeText(this, "neext page", Toast.LENGTH_SHORT).show();
        }
    }

    private fun proceedWithFunctionality() {
        // Your code to access camera, audio, location, and photos
    }

    private fun handlePermissionDenied() {
        // Inform the user that permissions are necessary for the app to function
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun checkPermissionButtonClicked() {
        checkPermissions()
    }
}
