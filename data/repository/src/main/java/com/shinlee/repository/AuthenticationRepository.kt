package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.model.LoginRequest
import com.shinlee.repository.model.LoginDataRep

interface AuthenticationRepository {
    suspend fun loginByEmail(loginRequest: LoginRequest): Result<LoginDataRep>
}