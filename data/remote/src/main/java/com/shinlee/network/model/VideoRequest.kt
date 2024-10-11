package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class VideoRequest(
    @SerializedName("page")
    val page: Int
)