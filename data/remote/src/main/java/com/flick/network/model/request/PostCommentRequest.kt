package com.flick.network.model.request

import com.google.gson.annotations.SerializedName

data class PostCommentRequest(
    @SerializedName("id")
    val videoId: Long,
    @SerializedName("content")
    val content: String
)
