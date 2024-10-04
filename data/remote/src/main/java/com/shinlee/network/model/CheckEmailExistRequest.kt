package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class CheckEmailExistRequest (
    @SerializedName("email")
    val email: String
)