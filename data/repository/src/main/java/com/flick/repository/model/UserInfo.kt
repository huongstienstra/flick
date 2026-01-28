package com.flick.repository.model

data class UserInfo(
    val id: String,
    val email: String,
    val phone: String,
    val profile: List<Profile> = emptyList()
) {
    data class Profile(
        val nickName: String
    )
}