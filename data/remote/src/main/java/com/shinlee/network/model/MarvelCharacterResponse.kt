package com.shinlee.network.model

import com.google.gson.annotations.SerializedName

data class MarvelCharacterResponse(
    @SerializedName("results")
    val listCharacter: List<Character>? = null
) {
    data class Character(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("description")
        val description: String? = null
    ) {
        data class Thumbnail(
            @SerializedName("path")
            val path: String? = null,
            @SerializedName("extension")
            val extension: String? = null
        ) {
            fun getPathURl(): String {
                if (!path.isNullOrEmpty() && !extension.isNullOrEmpty()) {
                    return "$path.$extension"
                }
                return ""
            }
        }
    }
}
