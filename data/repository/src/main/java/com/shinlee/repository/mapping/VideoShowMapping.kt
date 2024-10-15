package com.shinlee.repository.mapping

import com.shinlee.repository.model.VideoShow
import com.shinlee.repository.model.VideoInfo

fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
    return this.map { parser ->
        VideoShow(
            id = parser.id,
            description = parser.description,
            videoUrl = parser.videoUrl,
            thumbnailUrl = parser.thumbnailUrl,
            voteCount = parser.voteCount ?: 0,
            commentCount = parser.commentCount ?: 0,
            profileId = parser.profileId,
            profilePhoto = parser.profilePhoto,
            profileNickname = parser.profileNickname ?: "",
            contestId = parser.contestId,
            contestTitle = parser.contestTitle,
            contestImage = parser.contestImage,
            tags = parser.tags?.toTagList(),
            isFavourite = parser.isFavourite == 1
        )
    }
}

fun List<VideoInfo.Tag>.toTagList(): List<VideoShow.Tag> {
    return this.map { tag ->
        VideoShow.Tag(
            id = tag.id,
            content = tag.content
        )
    }
}
