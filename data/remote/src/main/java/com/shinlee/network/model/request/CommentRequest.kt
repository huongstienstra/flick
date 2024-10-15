package com.shinlee.network.model.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("id")
    val videoId: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int
)