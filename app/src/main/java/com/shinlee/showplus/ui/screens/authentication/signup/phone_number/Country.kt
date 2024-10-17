package com.shinlee.showplus.ui.screens.authentication.signup.phone_number

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("phone_code")
    val phoneCode: String,
    @SerializedName("flag_url")
    val flagUrl: String
)
