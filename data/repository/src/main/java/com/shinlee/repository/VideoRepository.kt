package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>>

}