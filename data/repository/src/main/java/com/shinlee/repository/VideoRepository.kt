package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(): Result<List<VideoInfo>>

}