package com.flick.network.model

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val statusData: Boolean = false,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("title")
    val title: String? = null,
)