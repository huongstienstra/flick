package com.shinlee.repository.mapping

import com.shinlee.network.model.response.CheckEmailExistResponse
import com.shinlee.repository.model.CheckEmailExistEntity


fun CheckEmailExistResponse.toCheckEmailExistEntity(): CheckEmailExistEntity {
    return CheckEmailExistEntity(
        statusData = this.statusData,
        isExist = this.isExist
    )
}