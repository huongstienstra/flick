package com.shinlee.repository

import com.shinlee.network.ApiResult
import com.shinlee.network.api.ShowPlusApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.repository.mapping.toMarvelCharacter
import com.shinlee.repository.model.MarvelCharacter

class MarvelRepositoryImpl(
    private val marvelApiService: ShowPlusApiService
) : MarvelRepository {
    override suspend fun getCharacters(
        apiKey: String,
        timestamp: String,
        hash: String
    ): ApiResult<List<MarvelCharacter>> {
        val response = safeApiCall {
            marvelApiService.getCharacters(
                apiKey, timestamp, hash
            )
        }

        return when (response) {
            is ApiResult.Success -> {
                // Map the list of MarvelCharacterDto to the list of MarvelCharacter
                val characters =
                    response.data.data?.results?.map { it.toMarvelCharacter() } ?: emptyList()
                ApiResult.success(characters)
            }

            is ApiResult.Error -> {
                ApiResult.error(response.throwable)
            }
        }

    }
}