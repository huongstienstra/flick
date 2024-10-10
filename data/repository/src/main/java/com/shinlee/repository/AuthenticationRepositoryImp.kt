package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.LoginRequest
import com.shinlee.network.model.RegisterRequest
import com.shinlee.network.model.RegistrationRequest
import com.shinlee.repository.mapping.toCheckEmailExistEntity
import com.shinlee.repository.mapping.toEntity
import com.shinlee.repository.mapping.toRegisterEntity
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.RegistrationEntity
import com.shinlee.repository.model.RegisterEntity

class AuthenticationRepositoryImp(private val apiService: ShowPlusApiService) :
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

    override suspend fun registerByEmail(
        email: String,
        password: String,
        passwordConfirm: String
    ): ApiResult<RegisterEntity> {
        val response = safeApiCall {
            apiService.registerWithEmail(RegisterRequest(email, password, passwordConfirm))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val registerData = response.data.toRegisterEntity()
                ApiResult.success(registerData)
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
