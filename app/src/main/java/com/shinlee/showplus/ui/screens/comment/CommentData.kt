package com.shinlee.showplus.ui.screens.comment

data class CommentData(
    val id: Int = -1,
    val username: String = "",
    val comment: String = "",
    val timestamp: String = "",
    val likes: Int = -1,
    val profileImage: String = "",
    val showReplyButton: Boolean = false,
    val replies: List<ReplyData> = emptyList(),
    val totalReplies: Int = 0
)

data class ReplyData(
    val id: Int,
    val username: String,
    val replyTo: String,
    val comment: String,
    val timestamp: String,
    val likes: Int,
    val profileImage: String
)