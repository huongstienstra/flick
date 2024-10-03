package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("result")
    val videos: List<VideoInfo>? = emptyList()
) : BaseResponse() {
    data class VideoInfo(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("video")
        val videoLink: String? = null,
        @SerializedName("thumbnail")
        val thumbnail: String? = null
    )
}
