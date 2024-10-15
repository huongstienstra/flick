package com.shinlee.network.model.response

import com.google.gson.annotations.SerializedName
import com.shinlee.network.model.BaseResponse

data class CheckEmailExistResponse(
    @SerializedName("is_exist")
    val isExist: Boolean
) : BaseResponse()