package com.shinlee.repository.model


data class LoginEntity(
    val token: String,
    val userInfo: UserInfoEntity
){
    data class UserInfoEntity(
        val id: String? = null,
        val email: String? = null,
        val phone: String = ""
    )
}





