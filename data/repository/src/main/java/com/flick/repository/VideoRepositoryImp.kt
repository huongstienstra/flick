package com.flick.repository

import com.flick.local.LocalComment
import com.flick.local.VideoInteractionStorage
import com.flick.network.ApiResult
import com.flick.network.api.PexelsApiService
import com.flick.repository.mapping.toVideoInfoList
import com.flick.repository.model.Comment
import com.flick.repository.model.VideoInfo

/**
 * Video repository implementation using Pexels API
 * Likes and comments are stored locally since Pexels doesn't support user interactions
 */
class VideoRepositoryImp(
    private val pexelsApiService: PexelsApiService,
    private val videoInteractionStorage: VideoInteractionStorage
) : VideoRepository {

    override suspend fun getVideos(page: Int, pageSize: Int): ApiResult<List<VideoInfo>> {
        return try {
            val response = pexelsApiService.getPopularVideos(
                page = page,
                perPage = pageSize
            )

            val likedIds = videoInteractionStorage.getLikedVideoIds()
            val videos = response.videos.toVideoInfoList(
                favouriteIds = likedIds,
                voteCounts = likedIds.associateWith { 1 },
                commentCounts = response.videos.associate {
                    it.id to videoInteractionStorage.getCommentCount(it.id)
                }
            )

            ApiResult.success(videos)
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }

    override suspend fun searchVideos(
        query: String,
        page: Int,
        pageSize: Int
    ): ApiResult<List<VideoInfo>> {
        return try {
            val response = pexelsApiService.searchVideos(
                query = query,
                orientation = "portrait",
                page = page,
                perPage = pageSize
            )

            val likedIds = videoInteractionStorage.getLikedVideoIds()
            val videos = response.videos.toVideoInfoList(
                favouriteIds = likedIds,
                voteCounts = likedIds.associateWith { 1 },
                commentCounts = response.videos.associate {
                    it.id to videoInteractionStorage.getCommentCount(it.id)
                }
            )

            ApiResult.success(videos)
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }

    override suspend fun likeVideo(videoId: Long): ApiResult<Boolean> {
        return try {
            val isNowLiked = videoInteractionStorage.toggleLike(videoId)
            ApiResult.success(isNowLiked)
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }

    override suspend fun isVideoLiked(videoId: Long): Boolean {
        return videoInteractionStorage.isVideoLiked(videoId)
    }

    override suspend fun getComments(
        videoId: Long,
        page: Int,
        pageSize: Int
    ): ApiResult<List<Comment>> {
        return try {
            val localComments = videoInteractionStorage.getLocalComments(videoId)

            // Paginate local comments
            val startIndex = (page - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, localComments.size)

            val comments = if (startIndex < localComments.size) {
                localComments.subList(startIndex, endIndex).map { it.toComment() }
            } else {
                emptyList()
            }

            ApiResult.success(comments)
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }

    override suspend fun postComment(videoId: Long, content: String): ApiResult<Comment?> {
        return try {
            val localComment = videoInteractionStorage.addComment(videoId, content)
            ApiResult.success(localComment.toComment())
        } catch (e: Exception) {
            ApiResult.error(e)
        }
    }

    private fun LocalComment.toComment(): Comment {
        return Comment(
            id = this.id.toInt(),
            content = this.content,
            videoId = this.videoId.toInt(),
            date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date(this.createdAt)),
            repliesCount = 0,
            profileName = this.userName,
            profileAvatar = "",
            likeCount = 0
        )
    }
}
