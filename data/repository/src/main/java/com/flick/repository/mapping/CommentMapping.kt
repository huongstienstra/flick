package com.flick.repository.mapping

import com.flick.network.model.response.CommentResponse
import com.flick.network.model.response.CommentResponseParse
import com.flick.repository.model.Comment

fun CommentResponse.toCommentList(): List<Comment> {
    return this.comments.mapNotNull { commentParse ->
        commentParse.id?.let { id ->
            Comment(
                id = id,
                content = commentParse.content ?: "",
                videoId = commentParse.videoId,
                date = commentParse.createdAt ?: commentParse.updatedAt ?: "",
                repliesCount = commentParse.repliesCount ?: 0,
                profileName = commentParse.profile?.nickname ?: "",
                profileAvatar = commentParse.profile?.photo ?: "",
                likeCount = commentParse.likeCount ?: 0
            )
        }
    }
}

fun CommentResponseParse.toComment(): Comment {
    return Comment(
        id = id ?: -1,
        content = content ?: "",
        videoId = videoId,
        date = createdAt ?: updatedAt ?: "",
        repliesCount = repliesCount ?: 0,
        profileName = profile?.nickname ?: "",
        profileAvatar = profile?.photo ?: "",
        likeCount = likeCount ?: 0
    )
}