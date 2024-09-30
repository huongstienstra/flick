package com.shinlee.showplus.ui.screens.authentication.login

import java.io.Serializable


data class LoginDataUI(
    val token: String,
    val userInfo: UserInfoUI
): Serializable

data class UserInfoUI(
    val id: String? = null,
    val email: String? = null
) : Serializable
