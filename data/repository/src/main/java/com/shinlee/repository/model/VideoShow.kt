package com.shinlee.repository.model

data class VideoShow(
    val id: Int? = null,
    val description: String? = null,
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val voteCount: Int = 0,
    val commentCount: Int = 0,
    val profileId: Int? = null,
    val profilePhoto: String? = null,
    val profileNickname: String = "",
    val contestId: Int? = null,
    val contestTitle: String? = null,
    val contestImage: String? = null,
    val tags: List<Tag>? = null,
    var isFavourite: Boolean
) {
    data class Tag(
        val id: Int? = null,
        val content: String? = null
    )
}