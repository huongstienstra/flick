package com.flick.network.api

import com.flick.network.model.pexels.PexelsVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service for Pexels Video API
 * Documentation: https://www.pexels.com/api/documentation/#videos
 */
interface PexelsApiService {

    /**
     * Get popular videos
     * @param page The page number (default 1)
     * @param perPage Number of videos per page (default 15, max 80)
     */
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): PexelsVideoResponse

    /**
     * Search for videos
     * @param query The search query
     * @param orientation Filter by orientation: landscape, portrait, square
     * @param size Filter by size: large (4K), medium (Full HD), small (HD)
     * @param page The page number (default 1)
     * @param perPage Number of videos per page (default 15, max 80)
     */
    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("orientation") orientation: String? = "portrait",
        @Query("size") size: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): PexelsVideoResponse
}
