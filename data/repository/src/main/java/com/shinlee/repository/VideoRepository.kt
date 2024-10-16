package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.repository.model.Comment
import com.shinlee.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>>
    suspend fun likeVideo(videoId: Long): ApiResult<Boolean>
    suspend fun getComments(videoId: Long, page: Int, pageSize: Int): ApiResult<List<Comment>>
    suspend fun postComment(videoId: Long, content: String): ApiResult<Comment?>
}