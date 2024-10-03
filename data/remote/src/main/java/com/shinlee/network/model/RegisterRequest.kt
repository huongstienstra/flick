package com.shinlee.network.model

data class RegisterRequest (
    val email: String,
    val password: String = "",
    val password_confirm: String = ""
)