package com.shinlee.showplus.ui.screens.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shinlee.showplus.ARG_SHOW_CONFIRM_PASSWORD
import com.shinlee.showplus.MainActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.databinding.FragmentInputOtpBinding
import com.shinlee.showplus.extension.showExitConfirmationDialog
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.LoginFragment
import com.shinlee.showplus.ui.screens.authentication.signup.confirm_password.ConfirmPasswordFragment
import com.shinlee.showplus.ui.screens.authentication.signup.nick_name.InputNickNameFragment
import com.shinlee.showplus.ui.screens.authentication.signup.otp.OTPScreenFragment
import com.shinlee.showplus.ui.screens.authentication.signup.phone_number.InputPhoneNumberFragment

class AuthenticationActivity : AppCompatActivity(), AuthNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (savedInstanceState == null) {
            addFragment(LoginFragment())
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    @SuppressLint("CommitTransaction")
    private fun addFragment(fragment: Fragment, args: Bundle? = null) {
        try {
            args?.let { fragment.arguments = it }

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,   // popEnter
                    R.anim.slide_out_right  // popExit
                )
                .add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .addToBackStack("auth_stack")
                .commit()
        } catch (e: IllegalStateException) {
            Log.e("FragmentTransaction", "Error in fragment transaction", e)
        }
    }

    override fun navigateLogin() {
        addFragment(LoginFragment())
    }

    override fun navigateToConfirmPassword(isShowConfirmPassword: Boolean) {
        val bundle = Bundle().apply {
            putBoolean(ARG_SHOW_CONFIRM_PASSWORD, isShowConfirmPassword)
        }
        addFragment(ConfirmPasswordFragment(), bundle)
    }

    override fun navigateToInputPhoneNumber() {
        addFragment(InputPhoneNumberFragment())
    }

    override fun navigateToInputNickName() {
        addFragment(InputNickNameFragment())
    }

    override fun navigateInputOTP() {
        addFragment(OTPScreenFragment())
    }

    override fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun handleBackPress() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is InputNickNameFragment) {
            currentFragment.showExitConfirmationDialog(
                title = com.shinlee.common.R.string.dialog_return_complete_nick_name_title,
                desc = com.shinlee.common.R.string.dialog_return_complete_nick_name_desc,
                positiveBtn = com.shinlee.common.R.string.dialog_return_complete_nick_name_positive,
                negativeBtn = com.shinlee.common.R.string.dialog_return_complete_nick_name_negative,
                onNext = {
                    navigateToMain()
                },
                onCancel = {

                }
            )
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun navigateUp() {
        handleBackPress()
    }
}