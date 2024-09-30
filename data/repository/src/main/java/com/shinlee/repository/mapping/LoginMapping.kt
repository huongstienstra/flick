package com.shinlee.repository.mapping

import com.shinlee.network.model.LoginResponse
import com.shinlee.repository.model.LoginEntity

fun LoginResponse.toEntity(): LoginEntity {
    return LoginEntity(
        token = this.token,
        userInfo = LoginEntity.UserInfoEntity(
            id = this.userInfoResponse.id,
            email = this.userInfoResponse.email
        )
    )
}