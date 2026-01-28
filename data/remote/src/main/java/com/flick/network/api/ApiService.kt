package com.flick.network.api

import com.flick.network.model.request.CheckEmailExistRequest
import com.flick.network.model.request.CommentRequest
import com.flick.network.model.request.FirebaseToken
import com.flick.network.model.response.CheckEmailExistResponse
import com.flick.network.model.request.LoginRequest
import com.flick.network.model.request.RegistrationRequest
import com.flick.network.model.response.RegistrationResponse
import com.flick.network.model.response.VideoResponse
import com.flick.network.model.request.LikeVideoRequest
import com.flick.network.model.request.PostCommentRequest
import com.flick.network.model.response.CommentResponse
import com.flick.network.model.response.LikeVideoResponse
import com.flick.network.model.response.PostCommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @GET("/api/videos")
    suspend fun getVideos(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<VideoResponse>

    @POST("api/videos/vote")
    suspend fun likeVideo(@Body likeVideoRequest: LikeVideoRequest): Response<LikeVideoResponse>

    @GET("api/videos/comments")
    suspend fun getComments(
        @Query("id") videoId: Long,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<CommentResponse>

    @POST("api/videos/add-comment")
    suspend fun postComment(@Body postComment: PostCommentRequest): Response<PostCommentResponse>

    @POST("/auth/login-with-email")
    suspend fun loginByEmail(@Body loginRequest: LoginRequest): Response<RegistrationResponse>

    @POST("auth/login-with-phone")
    suspend fun loginByPhone(@Body loginRequest: FirebaseToken): Response<RegistrationResponse>

    @POST("/auth/check-exist-email")
    suspend fun checkEmailExist(@Body email: CheckEmailExistRequest): Response<CheckEmailExistResponse>

    @POST("api/register-second-step")
    suspend fun submitNickNameAndInviteCode(@Body registrationRequest: RegistrationRequest): Response<RegistrationResponse>
}