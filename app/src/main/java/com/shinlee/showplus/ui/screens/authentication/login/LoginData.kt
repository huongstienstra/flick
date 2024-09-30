package com.shinlee.showplus.ui.screens.authentication.login

import java.io.Serializable


data class LoginData(
    val token: String,
    val userInfo: UserInfo
): Serializable{
    data class UserInfo(
        val id: String? = null,
        val email: String? = null
    ) : Serializable
}


