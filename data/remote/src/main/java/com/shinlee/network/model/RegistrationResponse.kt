package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("user_info")
    val userInfoResponse: UserInfoResponse? = null
) : BaseResponse() {
    data class UserInfoResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("email")
        val email: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("third_party_auth")
        val thirdPartyAuth: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("status")
        val status: String,
        @SerializedName("candy")
        val candy: Int,
        @SerializedName("invite_code")
        val inviteCode: String?,
        @SerializedName("profile")
        val profile: List<Profile>
    ) {
        data class Profile(
            @SerializedName("nickname")
            val nickname: String
        )
    }
}