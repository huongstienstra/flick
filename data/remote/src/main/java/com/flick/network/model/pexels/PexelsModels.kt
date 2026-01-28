package com.flick.network.model.pexels

import com.google.gson.annotations.SerializedName

/**
 * Response from Pexels Videos API
 * Used for both /videos/popular and /videos/search endpoints
 */
data class PexelsVideoResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("videos")
    val videos: List<PexelsVideo>,
    @SerializedName("next_page")
    val nextPage: String? = null
)

/**
 * Individual video data from Pexels
 */
data class PexelsVideo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("user")
    val user: PexelsUser,
    @SerializedName("video_files")
    val videoFiles: List<PexelsVideoFile>,
    @SerializedName("video_pictures")
    val videoPictures: List<PexelsVideoPicture>? = null
)

/**
 * User/creator info from Pexels
 */
data class PexelsUser(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

/**
 * Video file variant with quality and format info
 */
data class PexelsVideoFile(
    @SerializedName("id")
    val id: Long,
    @SerializedName("quality")
    val quality: String,
    @SerializedName("file_type")
    val fileType: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("fps")
    val fps: Double? = null,
    @SerializedName("link")
    val link: String
)

/**
 * Video thumbnail/picture preview
 */
data class PexelsVideoPicture(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nr")
    val nr: Int,
    @SerializedName("picture")
    val picture: String
)
