package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.VideoRequest
import com.shinlee.repository.mapping.toVideoInfo
import com.shinlee.repository.model.VideoInfo
import com.shinlee.network.ApiResult as Result

class VideoRepositoryImp(private val apiService: ShowPlusApiService) : VideoRepository {
    override suspend fun getVideos(page: Int): ApiResult<List<VideoInfo>> {
        val response = safeApiCall {
            apiService.getVideos(page = 1)
        }

        return when (response) {

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }
            is ApiResult.Success -> {
                val videos = response.data.videos?.toVideoInfo() ?: emptyList()
                ApiResult.success(videos)
            }
        }
    }

}