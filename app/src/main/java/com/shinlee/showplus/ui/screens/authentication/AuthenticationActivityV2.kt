package com.shinlee.showplus.ui.screens.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shinlee.showplus.MainActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginFragment
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.firststep.FirstStepSignUpFragment
import com.shinlee.showplus.ui.screens.authentication.signup.SignUpFragment
import com.shinlee.showplus.ui.screens.authentication.signup.secondstep.SecondStepSignupFragment
import com.shinlee.showplus.ui.screens.authentication.term.TermFragment

class AuthenticationActivityV2 : AppCompatActivity(), AuthNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }

    override fun navigateSignUp() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                SignUpFragment(),
                SignUpFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateTerm() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                TermFragment(),
                TermFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToFirstStepSignup() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                FirstStepSignUpFragment(),
                FirstStepSignUpFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToSecondStepSignup() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                SecondStepSignupFragment(),
                SecondStepSignupFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}