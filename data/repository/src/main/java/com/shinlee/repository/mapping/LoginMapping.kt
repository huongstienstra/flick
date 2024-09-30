package com.shinlee.repository.mapping

import com.shinlee.network.model.LoginResponse
import com.shinlee.repository.model.LoginDataRep
import com.shinlee.repository.model.UserInfoRep

fun LoginResponse.toLoginDataRep(): LoginDataRep {
    return LoginDataRep(
        token = this.token,
        userInfo = UserInfoRep(
            id = this.userInfo.id,
            email = this.userInfo.email
        )
    )
}