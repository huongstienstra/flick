package com.flick.repository.model

data class RegistrationEntity(
    val token: String,
    val userInfo: UserInfo? = null
)



