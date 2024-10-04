package com.shinlee.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.shinlee.network.Result
import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.CheckEmailExistResponse
import com.shinlee.network.model.LoginRequest
import com.shinlee.network.model.RegisterRequest
import com.shinlee.repository.model.CheckEmailExistEntity
import com.shinlee.repository.model.LoginEntity
import com.shinlee.repository.model.RegisterEntity

interface AuthenticationRepository {
    suspend fun loginByEmail(loginRequest: LoginRequest): Result<LoginEntity>
    suspend fun registerByEmail(email: String, password: String, passwordConfirm: String): Result<RegisterEntity>
    suspend fun checkEmailExist(email: String ): Result<CheckEmailExistEntity>
}