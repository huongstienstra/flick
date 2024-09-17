package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.network.model.MarvelCharacterResponse

interface MarvelRepository {
    suspend fun getCharacters(
        apiKey: String,
        timestamp: String,
        hash: String
    ): Result<MarvelCharacterResponse>

}