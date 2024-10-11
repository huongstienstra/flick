package com.shinlee.common.composable.comments

data class CommentData(
    val id: String,
    val username: String,
    val comment: String,
    val timestamp: String,
    val likes: String,
    val profileImage: Int,
    val showReplyButton: Boolean,
    val replies: List<ReplyData> = emptyList(),
    val totalReplies: Int = 0
)

data class ReplyData(
    val id: String,
    val username: String,
    val replyTo: String,
    val comment: String,
    val timestamp: String,
    val likes: String,
    val profileImage: Int
)