package com.shinlee.network.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirm")
    val confirmPassword: String,
    @SerializedName("firebase_token")
    val firebaseToken: String
)