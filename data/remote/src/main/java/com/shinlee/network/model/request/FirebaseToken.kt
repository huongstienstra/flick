package com.shinlee.network.model.request

import com.google.gson.annotations.SerializedName

data class FirebaseToken(
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("firebase_token")
    val firebaseToken: String
)