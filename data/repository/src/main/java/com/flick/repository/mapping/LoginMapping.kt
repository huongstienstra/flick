package com.flick.repository.mapping

import com.flick.network.model.response.CheckEmailExistResponse
import com.flick.network.model.response.RegistrationResponse
import com.flick.repository.model.CheckEmailExistEntity
import com.flick.repository.model.RegistrationEntity
import com.flick.repository.model.UserInfo

fun RegistrationResponse.toEntity(): RegistrationEntity {
    return RegistrationEntity(
        token = this.token,
        userInfo = this.userInfoResponse?.toUserInfoEntity()
    )
}

fun RegistrationResponse.UserInfoResponse.toUserInfoEntity(): UserInfo {
    return UserInfo(
        id = this.id.toString(),
        email = this.email ?: "",
        phone = this.phone ?: "",
        profile = if (this.profile != null) this.profile!!.map { it.toProfileEntity() } else emptyList()
    )
}

fun RegistrationResponse.UserInfoResponse.Profile.toProfileEntity(): UserInfo.Profile {
    return UserInfo.Profile(
        nickName = this.nickname ?: ""
    )
}

fun CheckEmailExistResponse.toCheckEmailExistEntity(): CheckEmailExistEntity {
    return CheckEmailExistEntity(
        statusData = this.statusData,
        isExist = this.isExist
    )
}