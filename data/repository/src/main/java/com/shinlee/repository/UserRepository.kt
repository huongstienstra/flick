package com.shinlee.repository

import com.shinlee.repository.model.UserInfo

interface UserRepository {
    suspend fun saveUserInformation(userInfo: UserInfo): Boolean
    suspend fun getUserInformation(): UserInfo?
}