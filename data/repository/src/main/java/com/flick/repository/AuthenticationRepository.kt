package com.flick.repository

import android.content.RestrictionEntry
import com.flick.network.ApiResult
import com.flick.repository.model.CheckEmailExistEntity
import com.flick.repository.model.RegistrationEntity

interface AuthenticationRepository {
    suspend fun loginWithEmail(email: String, password: String, passwordConfirm: String, firebaseToken: String): ApiResult<RegistrationEntity>
    suspend fun loginWithPhone(idToken: String, firebaseToken: String): ApiResult<RegistrationEntity>
    suspend fun submitNickNameAndInviteCode(nickName: String, inviteCode: String): ApiResult<RegistrationEntity>
    suspend fun checkEmailExist(email: String ): ApiResult<CheckEmailExistEntity>
}