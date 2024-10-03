package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class CheckEmailExistResponse(
    @SerializedName("status")
    val statusData: Boolean = false,
    @SerializedName("is_exist")
    val isExist: Boolean
) : BaseResponse()