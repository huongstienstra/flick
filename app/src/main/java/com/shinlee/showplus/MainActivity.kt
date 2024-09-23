package com.shinlee.showplus

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shinlee.showplus.ui.screens.contest.ContestFragment
import com.shinlee.showplus.ui.screens.profile.ProfileFragment
import com.shinlee.showplus.ui.screens.search.SearchFragment
import com.shinlee.showplus.ui.screens.show.ShowFragment
import com.shinlee.showplus.ui.screens.upload.UploadFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MarvelViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(ShowFragment())
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.showFragment -> selectedFragment = ShowFragment()
                R.id.contestFragment -> selectedFragment = ContestFragment()
                R.id.uploadFragment -> selectedFragment = UploadFragment()
                R.id.searchFragment -> selectedFragment = SearchFragment()
                R.id.profileFragment -> selectedFragment = ProfileFragment()
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragment, fragment.tag)
            .commitAllowingStateLoss()
    }


    private fun handleBackPress() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment !is ShowFragment) {
            findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId =
                R.id.showFragment
            loadFragment(ShowFragment())
        } else {
            finish()
        }
    }
}

