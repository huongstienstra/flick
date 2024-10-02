package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status")
    val statusData: Boolean = false,
    @SerializedName("token")
    val token: String
) : BaseResponse()
