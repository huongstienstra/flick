package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.repository.model.Comment
import com.shinlee.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>>
    suspend fun likeVideo(videoId: Int): ApiResult<Boolean>
    suspend fun getComments(videoId: Int, page: Int, pageSize: Int): ApiResult<List<Comment>>
}