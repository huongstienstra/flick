package com.flick.network.model.request

import com.google.gson.annotations.SerializedName

data class LikeVideoRequest(
    @SerializedName("id")
    val videoId: Long
)
