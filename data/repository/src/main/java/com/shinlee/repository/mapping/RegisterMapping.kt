package com.shinlee.repository.mapping

import com.shinlee.network.model.RegisterResponse
import com.shinlee.repository.model.RegisterEntity

fun RegisterResponse.toRegisterEntity(): RegisterEntity {
    return RegisterEntity(
        token = this.token,
        statusData = this.statusData
    )
}