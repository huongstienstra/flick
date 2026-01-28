package com.flick.repository.model

data class VideoInfo(
    val id: Long? = null,
    val description: String? = null,
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val voteCount: Int? = null,
    val commentCount: Int? = null,
    val profileId: Int? = null,
    val profilePhoto: String? = null,
    val profileNickname: String? = null,
    val contestId: Int? = null,
    val contestTitle: String? = null,
    val contestImage: String? = null,
    val tags: List<Tag>? = null,
    val isFavourite: Int
) {
    data class Tag(
        val id: Int? = null,
        val content: String? = null
    )
}