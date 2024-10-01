package com.shinlee.showplus.ui.screens.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginFragment
import com.shinlee.showplus.ui.screens.authentication.signup.firststep.FirstStepSignUpFragment
import com.shinlee.showplus.ui.screens.authentication.signup.SignUpFragment
import com.shinlee.showplus.ui.screens.authentication.term.TermFragment

class AuthenticationActivityV2 : AppCompatActivity(), FragmentNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment()) // Ensure LoginFragment is defined
                .commit()
        }
    }

    override fun navigateTo(destination: Destination) {
        val fragment = when (destination) {
            is Destination.LoginFragment -> LoginFragment()
            is Destination.SignUpFragment -> SignUpFragment()
            is Destination.TermFragment -> TermFragment()
            is Destination.FirstStepSignup -> FirstStepSignUpFragment()
        }

        fragment.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, it)
                .addToBackStack(null)
                .commit()
        }
    }
}