package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("invite_code")
    val inviteCode: String
)