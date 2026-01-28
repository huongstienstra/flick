package com.flick.repository

import com.flick.repository.model.UserInfo

interface UserRepository {
    suspend fun saveUserInformation(userInfo: UserInfo): Boolean
    suspend fun getUserInformation(): UserInfo?
}