package com.shinlee.network.api

import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.CheckEmailExistResponse
import com.shinlee.network.model.LoginRequest
import com.shinlee.network.model.MarvelCharacterResponseDto
import com.shinlee.network.model.RegisterRequest
import com.shinlee.network.model.RegisterResponse
import com.shinlee.network.model.RegistrationRequest
import com.shinlee.network.model.RegistrationResponse
import com.shinlee.network.model.VideoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShowPlusApiService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("ts") timestamp: String,
        @Query("hash") hash: String
    ): Response<MarvelCharacterResponseDto>

    @GET("/api/videos")
    suspend fun getVideos(@Query("page") page: Int): Response<VideoResponse>

    @POST("/auth/login-with-email")
    suspend fun loginByEmail(@Body loginRequest: LoginRequest): Response<RegistrationResponse>

    @POST("/auth/signup")
    suspend fun registerWithEmail(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("/auth/check-exist-email")
    suspend fun checkEmailExist(@Body email: CheckEmailExistRequest): Response<CheckEmailExistResponse>

    @POST("api/register-second-step")
    suspend fun submitNickNameAndInviteCode(@Body registrationRequest: RegistrationRequest): Response<RegistrationResponse>
}