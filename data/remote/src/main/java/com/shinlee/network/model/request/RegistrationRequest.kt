package com.shinlee.network.model.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("invite_code")
    val inviteCode: String
)