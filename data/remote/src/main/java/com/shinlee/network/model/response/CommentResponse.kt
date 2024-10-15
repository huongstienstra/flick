package com.shinlee.network.model.response

import com.google.gson.annotations.SerializedName
import com.shinlee.network.model.BaseResponse

data class CommentResponse(
    @SerializedName("data", alternate = ["result"])
    val comments: List<CommentResponseParse> = emptyList()
) : BaseResponse()

data class CommentResponseParse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("video_id")
    val videoId: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("replies_count")
    val repliesCount: Int? = null,
    @SerializedName("profile")
    val profile: Profile? = null,
    @SerializedName("last_three_profile_photos")
    val lastThreeProfilePhotos: List<String>? = null,
    @SerializedName("like_count")
    val likeCount: Int? = null
) {
    data class Profile(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("photo")
        val photo: String? = null,
        @SerializedName("nickname")
        val nickname: String? = null
    )
}