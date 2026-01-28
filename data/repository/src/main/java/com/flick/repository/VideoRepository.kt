package com.flick.repository

import com.flick.network.ApiResult
import com.flick.repository.model.Comment
import com.flick.repository.model.VideoInfo

interface VideoRepository {
    suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>>
    suspend fun searchVideos(query: String, page: Int, pageSize: Int): ApiResult<List<VideoInfo>>
    suspend fun likeVideo(videoId: Long): ApiResult<Boolean>
    suspend fun isVideoLiked(videoId: Long): Boolean
    suspend fun getComments(videoId: Long, page: Int, pageSize: Int): ApiResult<List<Comment>>
    suspend fun postComment(videoId: Long, content: String): ApiResult<Comment?>
}
