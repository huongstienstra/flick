package com.flick.repository.model

data class Comment(
    val id: Int,
    val content: String,
    val videoId: Int? = null,
    val date: String,
    val repliesCount: Int,
    val profileName: String,
    val profileAvatar: String,
    val likeCount: Int
)