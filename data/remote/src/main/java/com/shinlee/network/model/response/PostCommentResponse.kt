package com.shinlee.network.model.response

import com.google.gson.annotations.SerializedName
import com.shinlee.network.model.BaseResponse

data class PostCommentResponse(
    @SerializedName("data")
    val comment: CommentResponseParse? = null
): BaseResponse()
