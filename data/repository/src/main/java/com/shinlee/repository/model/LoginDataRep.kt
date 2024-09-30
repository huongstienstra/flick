package com.shinlee.repository.model


data class LoginDataRep(
    val token: String,
    val userInfo: UserInfoRep
)

data class UserInfoRep(
    val id: String? = null,
    val email: String? = null
)



