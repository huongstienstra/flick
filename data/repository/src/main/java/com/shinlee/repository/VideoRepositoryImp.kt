package com.shinlee.repository

import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.repository.mapping.toVideoInfo
import com.shinlee.repository.model.VideoInfo
import com.shinlee.network.Result as Result

class VideoRepositoryImp(private val apiService: ShowPlusApiService) : VideoRepository {
    override suspend fun getVideos(): Result<List<VideoInfo>> {
        val response = safeApiCall {
            apiService.getVideos()
        }

        return when (response) {

            is Result.Error -> {
                Result.error(response.throwable)
            }
            is Result.Success -> {
                val videos = response.data.videos?.toVideoInfo() ?: emptyList()
                Result.success(videos)
            }
        }
    }

}