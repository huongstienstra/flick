package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.model.LoginRequest
import com.shinlee.repository.model.LoginEntity

interface AuthenticationRepository {
    suspend fun loginByEmail(loginRequest: LoginRequest): Result<LoginEntity>
}