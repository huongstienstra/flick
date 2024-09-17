package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.api.MarvelApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.repository.mapping.toMarvelCharacter
import com.shinlee.repository.model.MarvelCharacter

class MarvelRepositoryImpl(
    private val marvelApiService: MarvelApiService
) : MarvelRepository {
    override suspend fun getCharacters(
        apiKey: String,
        timestamp: String,
        hash: String
    ): Result<List<MarvelCharacter>> {
        val response = safeApiCall {
            marvelApiService.getCharacters(
                apiKey, timestamp, hash
            )
        }

        return when (response) {
            is Result.Success -> {
                // Map the list of MarvelCharacterDto to the list of MarvelCharacter
                val characters =
                    response.data.data?.results?.map { it.toMarvelCharacter() } ?: emptyList()
                Result.success(characters)
            }

            is Result.Error -> {
                Result.error(response.throwable)
            }
        }

    }
}