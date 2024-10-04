package com.shinlee.repository.mapping

import com.shinlee.network.model.CheckEmailExistResponse
import com.shinlee.network.model.RegisterResponse
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.RegisterEntity


fun CheckEmailExistResponse.toCheckEmailExistEntity(): CheckEmailExistEntity {
    return CheckEmailExistEntity(
        statusData = this.statusData,
        isExist = this.isExist
    )
}