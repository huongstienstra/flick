package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.RegistrationEntity
import com.shinlee.repository.model.RegisterEntity

interface AuthenticationRepository {
    suspend fun loginWithEmail(email: String, password: String, passwordConfirm: String, firebaseToken: String): ApiResult<RegistrationEntity>
    suspend fun submitNickNameAndInviteCode(nickName: String, inviteCode: String): ApiResult<RegistrationEntity>

    suspend fun registerByEmail(email: String, password: String, passwordConfirm: String): ApiResult<RegisterEntity>
    suspend fun checkEmailExist(email: String ): ApiResult<CheckEmailExistEntity>
}