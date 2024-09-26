package com.shinlee.network.api

import com.shinlee.network.model.MarvelCharacterResponseDto
import com.shinlee.network.model.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowPlusApiService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("ts") timestamp: String,
        @Query("hash") hash: String
    ): Response<MarvelCharacterResponseDto>

    @GET("/api/videos")
    suspend fun getVideos(): Response<VideoResponse>
}