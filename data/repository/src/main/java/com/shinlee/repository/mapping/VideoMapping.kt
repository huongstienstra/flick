package com.shinlee.repository.mapping

import com.shinlee.network.model.VideoResponseParser
import com.shinlee.repository.VideoShow
import com.shinlee.repository.model.VideoInfo

fun List<VideoResponseParser>.toVideoInfo(): List<VideoInfo> {
    return this.map { parser ->
        VideoInfo(
            id = parser.id,
            videoUrl = parser.videoLink,
            description = parser.description,
            thumbnailUrl = parser.thumbnail,
            voteCount = parser.voteCount,
            commentCount = parser.commentCount,
            profileId = parser.profile?.id,
            profilePhoto = parser.profile?.photo,
            profileNickname = parser.profile?.nickname,
            contestId = parser.contest?.id,
            contestTitle = parser.contest?.title,
            contestImage = parser.contest?.image,
            tags = parser.tags?.toTagList(),
            isFavourite = parser.isFavorite ?: 0
        )
    }
}

fun List<VideoResponseParser.Tag>.toTagList(): List<VideoInfo.Tag> {
    return this.map { tag ->
        VideoInfo.Tag(
            id = tag.id,
            content = tag.content
        )
    }
}


