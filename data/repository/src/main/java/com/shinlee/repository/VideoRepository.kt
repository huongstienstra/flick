package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(): ApiResult<List<VideoInfo>>

}