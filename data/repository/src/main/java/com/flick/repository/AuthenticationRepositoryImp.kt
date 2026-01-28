package com.flick.repository

import android.content.RestrictionEntry
import com.flick.network.ApiResult
import com.flick.network.api.AuthApiService
import com.flick.network.handler.safeApiCall
import com.flick.network.model.request.CheckEmailExistRequest
import com.flick.network.model.request.FirebaseToken
import com.flick.network.model.request.LoginRequest
import com.flick.network.model.request.RegistrationRequest
import com.flick.repository.mapping.toCheckEmailExistEntity
import com.flick.repository.mapping.toEntity
import com.flick.repository.model.CheckEmailExistEntity
import com.flick.repository.model.RegistrationEntity

class AuthenticationRepositoryImp(private val apiService: AuthApiService) :
    AuthenticationRepository {

    override suspend fun loginWithEmail(
        email: String,
        password: String,
        passwordConfirm: String,
        firebaseToken: String
    ): ApiResult<RegistrationEntity> {
        val response = safeApiCall {
            apiService.loginByEmail(LoginRequest(email, password, passwordConfirm, firebaseToken))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val result = response.data.toEntity()
                ApiResult.success(result)
            }
        }
    }

    override suspend fun loginWithPhone(
        idToken: String,
        firebaseToken: String
    ): ApiResult<RegistrationEntity> {
        val response = safeApiCall {
            apiService.loginByPhone(FirebaseToken(idToken, firebaseToken))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val result = response.data.toEntity()
                ApiResult.success(result)
            }
        }
    }

    override suspend fun submitNickNameAndInviteCode(
        nickName: String,
        inviteCode: String
    ): ApiResult<RegistrationEntity> {
        val response = safeApiCall {
            apiService.submitNickNameAndInviteCode(RegistrationRequest(nickName, inviteCode))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val result = response.data.toEntity()
                ApiResult.success(result)
            }
        }
    }

    override suspend fun checkEmailExist(email: String): ApiResult<CheckEmailExistEntity> {
        val response = safeApiCall {
            apiService.checkEmailExist(CheckEmailExistRequest(email))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val emailCheckExistData = response.data.toCheckEmailExistEntity()
                ApiResult.success(emailCheckExistData)
            }
        }
    }
}
