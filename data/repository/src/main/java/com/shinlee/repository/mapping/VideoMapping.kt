package com.shinlee.repository.mapping

import com.shinlee.network.model.VideoResponse
import com.shinlee.repository.model.VideoInfo

fun List<VideoResponse.VideoInfo>.toVideoInfo(): List<VideoInfo> {
    return this.map {
        VideoInfo(
            id = it.id,
            videoUrl = it.videoLink,
            thumbnailUrl = it.thumbnail
        )
    }
}