package com.shinlee.showplus

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shinlee.showplus.ui.screens.authentication.AuthenticationActivityV2
import com.shinlee.showplus.ui.screens.contest.ContestFragment
import com.shinlee.showplus.ui.screens.profile.ProfileFragment
import com.shinlee.showplus.ui.screens.search.SearchFragment
import com.shinlee.showplus.ui.screens.show.ShowFragmentV2
import com.shinlee.showplus.ui.screens.upload.UploadFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: ShareViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(ShowFragmentV2())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.showFragment -> selectedFragment = ShowFragmentV2()
                R.id.contestFragment -> selectedFragment = ContestFragment()
                R.id.uploadFragment -> selectedFragment = UploadFragment()
                R.id.searchFragment -> selectedFragment = SearchFragment()
                R.id.profileFragment -> {
                    if (viewModel.checkForActiveSession()) {
                        selectedFragment = ProfileFragment()
                    } else showAlertDialog()

                }
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    @SuppressLint("CommitTransaction")
    private fun loadFragment(fragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .commit()
        } catch (e: IllegalStateException) {
            Log.e("FragmentTransaction", "Error in fragment transaction", e)
        }
    }


    private fun handleBackPress() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment !is ShowFragmentV2) {
            findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId =
                R.id.showFragment
            loadFragment(ShowFragmentV2())
        } else {
            finish()
        }
    }


    // just test
    private fun showAlertDialog() {
        // Create and configure the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login")
            .setMessage("Are you sure you want to Login")
            .setPositiveButton("OK") { dialog, _ ->
                startActivity(Intent(this@MainActivity, AuthenticationActivityV2::class.java))
                dialog.dismiss() // Close the dialog
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Close the dialog
            }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}

