package com.flick.app.ui.screens.authentication.nav

interface AuthNavigator {
    fun navigateLogin()
    fun navigateToMain()
    fun navigateToConfirmPassword(isShowConfirmPassword: Boolean = true)
    fun navigateToInputPhoneNumber()
    fun navigateToInputNickName()
    fun navigateInputOTP()
    fun navigateUp()
}