package com.shinlee.repository.mapping

import com.shinlee.network.model.response.CheckEmailExistResponse
import com.shinlee.network.model.response.RegistrationResponse
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.RegistrationEntity
import com.shinlee.repository.model.UserInfo

fun RegistrationResponse.toEntity(): RegistrationEntity {
    return RegistrationEntity(
        token = this.token,
        userInfo = this.userInfoResponse?.toUserInfoEntity()
    )
}

fun RegistrationResponse.UserInfoResponse.toUserInfoEntity(): UserInfo {
    return UserInfo(
        id = this.id.toString(),
        email = this.email,
        phone = this.phone ?: "",
        profile = this.profile.map { it.toProfileEntity() }
    )
}

fun RegistrationResponse.UserInfoResponse.Profile.toProfileEntity(): UserInfo.Profile {
    return UserInfo.Profile(
        nickName = this.nickname
    )
}

fun CheckEmailExistResponse.toCheckEmailExistEntity(): CheckEmailExistEntity {
    return CheckEmailExistEntity(
        statusData = this.statusData,
        isExist = this.isExist
    )
}