package com.flick.network.model.response

import com.google.gson.annotations.SerializedName
import com.flick.network.model.BaseResponse

data class RegistrationResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user_info")
    val userInfoResponse: UserInfoResponse? = null
) : BaseResponse() {
    data class UserInfoResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("email")
        val email: String? = null,
        @SerializedName("password")
        val password: String? = null,
        @SerializedName("third_party_auth")
        val thirdPartyAuth: String? = null,
        @SerializedName("phone")
        val phone: String? = null,
        @SerializedName("status")
        val status: String? = null,
        @SerializedName("candy")
        val candy: Int? = null,
        @SerializedName("invite_code")
        val inviteCode: String? = null,
        @SerializedName("profile")
        val profile: List<Profile>? = null
    ) {
        data class Profile(
            @SerializedName("nickname")
            val nickname: String? = null
        )
    }
}