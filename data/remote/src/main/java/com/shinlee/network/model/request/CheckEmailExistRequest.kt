package com.shinlee.network.model.request

import com.google.gson.annotations.SerializedName

data class CheckEmailExistRequest (
    @SerializedName("email")
    val email: String
)