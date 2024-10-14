package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("result")
    val videos: List<VideoResponseParser>? = emptyList()
) : BaseResponse()
