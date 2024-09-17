package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.api.MarvelApiService
import com.shinlee.network.handler.safeApiCall
import com.shinlee.network.model.MarvelCharacterResponse

class MarvelRepositoryImpl(
    private val marvelApiService: MarvelApiService
) : MarvelRepository {
    override suspend fun getCharacters(
        apiKey: String,
        timestamp: String,
        hash: String
    ): Result<MarvelCharacterResponse> {
        return safeApiCall {
            marvelApiService.getCharacters(
                apiKey, timestamp, hash
            )
        }

    }
}