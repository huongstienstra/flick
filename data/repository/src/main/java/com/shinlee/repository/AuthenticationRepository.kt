package com.shinlee.repository

import android.content.RestrictionEntry
import com.shinlee.network.ApiResult
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.RegistrationEntity

interface AuthenticationRepository {
    suspend fun loginWithEmail(email: String, password: String, passwordConfirm: String, firebaseToken: String): ApiResult<RegistrationEntity>
    suspend fun loginWithPhone(idToken: String, firebaseToken: String): ApiResult<RegistrationEntity>
    suspend fun submitNickNameAndInviteCode(nickName: String, inviteCode: String): ApiResult<RegistrationEntity>
    suspend fun checkEmailExist(email: String ): ApiResult<CheckEmailExistEntity>
}