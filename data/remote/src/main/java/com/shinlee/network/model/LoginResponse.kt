package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_info")
    val userInfo: UserInfo
) : BaseResponse() {
    data class UserInfo(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("email")
        val email: String? = null
    )
}
