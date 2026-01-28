package com.flick.network.model.response

import com.google.gson.annotations.SerializedName
import com.flick.network.model.BaseResponse

data class CheckEmailExistResponse(
    @SerializedName("is_exist")
    val isExist: Boolean
) : BaseResponse()