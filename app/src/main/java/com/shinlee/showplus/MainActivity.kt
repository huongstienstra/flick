package com.shinlee.showplus

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shinlee.common.views.CustomBottomNavigationView
import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.contest.ContestFragment
import com.shinlee.showplus.ui.screens.profile.ProfileFragment
import com.shinlee.showplus.ui.screens.search.SearchFragment
import com.shinlee.showplus.ui.screens.show.ShowFragment
import com.shinlee.showplus.ui.screens.upload.UploadFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainViewModel.checkLoginStatus()

        if (savedInstanceState == null) {
            loadFragment(ShowFragment())
        }

        val bottomNavigationView = findViewById<CustomBottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { itemId ->
            var selectedFragment: Fragment? = null
            when (itemId) {
                com.shinlee.common.R.id.nav_home -> selectedFragment = ShowFragment()
                com.shinlee.common.R.id.nav_contest -> selectedFragment = ContestFragment()
                com.shinlee.common.R.id.nav_upload -> selectedFragment = UploadFragment()
                com.shinlee.common.R.id.nav_search -> selectedFragment = SearchFragment()
                com.shinlee.common.R.id.nav_profile -> selectedFragment = ProfileFragment()
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

        if (currentFragment !is ShowFragment) {
//            findViewById<CustomBottomNavigationView>(R.id.bottom_navigation).selectedItemId =
//                R.id.showFragment
            loadFragment(ShowFragment())
        } else {
            finish()
        }
    }
}

