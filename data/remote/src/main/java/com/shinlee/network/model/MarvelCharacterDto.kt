package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class MarvelCharacterResponseDto(
    @SerializedName("copyright")
    val copyright: String? = null,
    @SerializedName("attributionText")
    val attributionText: String? = null,
    @SerializedName("attributionHTML")
    val attributionHTML: String? = null,
    @SerializedName("etag")
    val etag: String? = null,
    @SerializedName("data")
    val data: DataContainerDto? = null
): BaseResponse() {
    data class DataContainerDto(
        @SerializedName("offset")
        val offset: Int? = null,
        @SerializedName("limit")
        val limit: Int? = null,
        @SerializedName("total")
        val total: Int? = null,
        @SerializedName("count")
        val count: Int? = null,
        @SerializedName("results")
        val results: List<MarvelCharacterDto>? = null
    )

    data class MarvelCharacterDto(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("thumbnail")
        val thumbnail: ThumbnailDto? = null
    ) {
        data class ThumbnailDto(
            @SerializedName("path")
            val path: String? = null,
            @SerializedName("extension")
            val extension: String? = null
        ) {
            fun getThumbnailUrl(): String {
                return if (!path.isNullOrEmpty() && !extension.isNullOrEmpty()) {
                    "$path.$extension"
                } else {
                    ""
                }
            }
        }
    }
}
