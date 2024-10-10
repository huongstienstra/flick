package com.shinlee.showplus.ui.screens.show.mapping

import com.shinlee.repository.model.VideoInfo
import com.shinlee.showplus.ui.screens.show.VideoShow

fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
    return this.map {
        VideoShow(
            id = it.id ?: 0,
            videoLink = it.videoUrl ?: "",
            thumbnail = it.thumbnailUrl ?: ""
        )
    }
}
