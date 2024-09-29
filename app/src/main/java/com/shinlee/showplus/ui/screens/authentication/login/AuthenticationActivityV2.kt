package com.shinlee.showplus.ui.screens.authentication.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginFragment

class AuthenticationActivityV2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (savedInstanceState == null) {
            showLoginFragment()
        }
    }

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    fun showSignUpFragment() {
//        val signUpFragment = SignUpFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, signUpFragment)
//            .addToBackStack(null)
//            .commit()
    }

    fun navigateBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}