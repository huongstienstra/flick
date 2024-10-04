package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.LoginRequest
import com.shinlee.network.model.RegisterRequest
import com.shinlee.repository.mapping.toCheckEmailExistEntity
import com.shinlee.repository.mapping.toEntity
import com.shinlee.repository.mapping.toRegisterEntity
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.LoginEntity
import com.shinlee.repository.model.RegisterEntity

class AuthenticationRepositoryImp(private val apiService: ShowPlusApiService) :AuthenticationRepository {
    override suspend fun loginByEmail(loginRequest: LoginRequest): Result<LoginEntity> {
        val response = safeApiCall {
            apiService.loginByEmail(loginRequest)
        }
        return when (response) {

            is Result.Error -> {
                Result.error(response.throwable)
            }
            is Result.Success -> {
                val loginData = response.data.toEntity()
                Result.success(loginData)
            }
        }
    }

    override suspend fun registerByEmail(
        email: String,
        password: String,
        passwordConfirm: String
    ): Result<RegisterEntity> {
        val response = safeApiCall {
            apiService.registerWithEmail(RegisterRequest(email, password, passwordConfirm))
        }
        return when (response) {

            is Result.Error -> {
                Result.error(response.throwable)
            }
            is Result.Success -> {
                val registerData = response.data.toRegisterEntity()
                Result.success(registerData)
            }
        }
    }

    override suspend fun checkEmailExist(email: String): Result<CheckEmailExistEntity> {
        val response = safeApiCall {
            apiService.checkEmailExist(CheckEmailExistRequest(email))
        }
        return when (response) {

            is Result.Error -> {
                Result.error(response.throwable)
            }
            is Result.Success -> {
                val emailCheckExistData = response.data.toCheckEmailExistEntity()
                Result.success(emailCheckExistData)
            }
        }
    }
}
