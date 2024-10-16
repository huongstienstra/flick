package com.shinlee.showplus.ui.screens.show.mapping

import com.shinlee.showplus.ui.screens.comment.CommentData
import com.shinlee.repository.model.Comment
import com.shinlee.repository.model.VideoInfo
import com.shinlee.showplus.ui.screens.show.VideoShow

fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
    return this.map { parser ->
        VideoShow(
            id = parser.id ?: -1,
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

fun List<Comment>.toCommentDataList(): List<CommentData> {
    return this.map { comment ->
        comment.toCommentData()
    }
}

fun Comment.toCommentData(): CommentData {
    return CommentData(
        id = id,
        username = profileName,
        comment = content,
        timestamp = date,
        likes = likeCount,
        profileImage = profileAvatar,
        showReplyButton = repliesCount > 0,
        totalReplies = repliesCount
    )
}


