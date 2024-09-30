package com.shinlee.network.model

import com.shinlee.network.model.LoginResponse.UserInfo

data class LoginRequest (
    val email: String,
    val password: String
)