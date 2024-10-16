package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.request.CommentRequest
import com.shinlee.network.model.request.LikeVideoRequest
import com.shinlee.network.model.request.PostCommentRequest
import com.shinlee.repository.mapping.toComment
import com.shinlee.repository.mapping.toCommentList
import com.shinlee.repository.mapping.toVideoInfo
import com.shinlee.repository.model.Comment
import com.shinlee.repository.model.VideoInfo
import com.shinlee.network.ApiResult as Result

class VideoRepositoryImp(private val apiService: ShowPlusApiService) : VideoRepository {
    override suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>> {
        val response = safeApiCall {
            apiService.getVideos(page = page, pageSize = pageSize)
        }

        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val videos = response.data.videos.toVideoInfo()
                ApiResult.success(videos)
            }
        }
    }

    override suspend fun likeVideo(videoId: Long): ApiResult<Boolean> {
        val response = safeApiCall {
            apiService.likeVideo(LikeVideoRequest(videoId = videoId))
        }
        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                ApiResult.success(response.data.isLiked)
            }
        }
    }

    override suspend fun getComments(
        videoId: Long,
        page: Int,
        pageSize: Int
    ): ApiResult<List<Comment>> {
        val response = safeApiCall {
            apiService.getComments(videoId, page, pageSize)
        }

        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val comments = response.data.toCommentList()
                ApiResult.success(comments)
            }
        }
    }

    override suspend fun postComment(videoId: Long, content: String): ApiResult<Comment?> {
        val response = safeApiCall {
            apiService.postComment(PostCommentRequest(videoId, content))
        }

        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }

            is ApiResult.Success -> {
                val comments = response.data.comment
                ApiResult.success(comments?.toComment())
            }
        }
    }

}