package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("title")
    val title: String? = null,
)