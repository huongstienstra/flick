package com.shinlee.repository.mapping

import com.shinlee.network.model.response.CommentResponse
import com.shinlee.repository.model.Comment

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