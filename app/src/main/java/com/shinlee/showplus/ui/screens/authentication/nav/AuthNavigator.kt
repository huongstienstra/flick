package com.shinlee.showplus.ui.screens.authentication.nav

interface AuthNavigator {
    fun navigateSignUp()
    fun navigateTerm()
    fun navigateToFirstStepSignup()
    fun navigateToMain()
    fun navigateToSecondStepSignup(email: String)
    fun navigateToThirdStepSignup(token: String)
    fun navigateToLogin()
    fun onBackPress()
}