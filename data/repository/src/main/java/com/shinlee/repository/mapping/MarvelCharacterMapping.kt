package com.shinlee.repository.mapping

import com.shinlee.network.model.MarvelCharacterResponseDto
import com.shinlee.repository.model.MarvelCharacter


// Extension function to map MarvelCharacterDto to MarvelCharacter
fun MarvelCharacterResponseDto.MarvelCharacterDto.toMarvelCharacter(): MarvelCharacter {
    return MarvelCharacter(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        description = this.description.orEmpty(),
        thumbnailUrl = this.thumbnail?.getThumbnailUrl().orEmpty()
    )
}