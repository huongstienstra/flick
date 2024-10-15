package com.shinlee.network.model.response

import com.google.gson.annotations.SerializedName
import com.shinlee.network.model.BaseResponse

data class VideoResponseParser(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("video")
    val videoLink: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
    @SerializedName("comment_count")
    val commentCount: Int? = null,
    @SerializedName("profile")
    val profile: Profile? = null,
    @SerializedName("contest")
    val contest: Contest? = null,
    @SerializedName("tags")
    val tags: List<Tag>? = null,
    @SerializedName("is_favorite")
    val isFavorite: Int? = null
) {
    data class Profile(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("photo")
        val photo: String? = null,
        @SerializedName("nickname")
        val nickname: String? = null
    )

    data class Contest(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("image")
        val image: String? = null
    )

    data class Tag(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("content")
        val content: String? = null
    )
}

data class VideoResponse(
    @SerializedName("result")
    val videos: List<VideoResponseParser> = emptyList()
) : BaseResponse()
