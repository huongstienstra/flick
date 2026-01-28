package com.flick.app.ui.screens.show.mapping

import com.flick.app.ui.screens.comment.CommentData
import com.flick.repository.model.Comment
import com.flick.repository.model.VideoInfo
import com.flick.app.ui.screens.show.VideoShow

fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
    return this.map { it.toVideoShow() }
}

fun VideoInfo.toVideoShow(): VideoShow {
    return VideoShow(
        id = this.id ?: -1,
        description = this.description,
        videoUrl = this.videoUrl,
        thumbnailUrl = this.thumbnailUrl,
        voteCount = this.voteCount ?: 0,
        commentCount = this.commentCount ?: 0,
        profileId = this.profileId,
        profilePhoto = this.profilePhoto,
        profileNickname = this.profileNickname ?: "",
        contestId = this.contestId,
        contestTitle = this.contestTitle,
        contestImage = this.contestImage,
        tags = this.tags?.toTagList(),
        isFavourite = this.isFavourite == 1
    )
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


