package com.shinlee.repository.model

import com.shinlee.repository.model.LoginEntity.UserInfoEntity

data class RegisterEntity(
    val token: String,
    val statusData: Boolean
)
