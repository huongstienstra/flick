package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.LoginRequest
import com.shinlee.repository.mapping.toLoginDataRep
import com.shinlee.repository.model.LoginDataRep

class AuthenticationRepositoryImp(private val apiService: ShowPlusApiService) :AuthenticationRepository {
    override suspend fun loginByEmail(loginRequest: LoginRequest): Result<LoginDataRep> {
        val response = safeApiCall {
            apiService.loginByEmail(loginRequest)
        }

        return when (response) {

            is Result.Error -> {
                Result.error(response.throwable)
            }
            is Result.Success -> {
                val loginData = response.data.toLoginDataRep()
                Result.success(loginData)
            }
        }
    }
}