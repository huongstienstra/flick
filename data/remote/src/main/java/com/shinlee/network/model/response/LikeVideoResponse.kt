package com.shinlee.network.model.response

import com.google.gson.annotations.SerializedName
import com.shinlee.network.model.BaseResponse

data class LikeVideoResponse(
    @SerializedName("is_voted")
    val isLiked: Boolean
): BaseResponse()
