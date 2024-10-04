package com.shinlee.showplus.ui.screens.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shinlee.showplus.MainActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginFragment
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.email.InputEmailFragment
import com.shinlee.showplus.ui.screens.authentication.signup.SignUpFragment
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel
import com.shinlee.showplus.ui.screens.authentication.signup.confirm_password.ConfirmPasswordFragment
import com.shinlee.showplus.ui.screens.authentication.signup.phone_number.ThirdStepSignupFragment
import com.shinlee.showplus.ui.screens.authentication.term.TermFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            .add(
                R.id.fragment_container,
                SignUpFragment(),
                SignUpFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateTerm() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                TermFragment(),
                TermFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToFirstStepSignup() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                InputEmailFragment(),
                InputEmailFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToConfirmPassword() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                ConfirmPasswordFragment(),
                ConfirmPasswordFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToThirdStepSignup(token: String) {
        val bundle = Bundle().apply {
            putString("token", token)
        }
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                ThirdStepSignupFragment().apply {
                    arguments = bundle
                },
                ConfirmPasswordFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun navigateToLogin() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName
            )
            .addToBackStack("auth_stack")
            .commit()
    }

    override fun onBackPress() {
        super.onBackPressed()
    }


    override fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}