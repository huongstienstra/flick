package com.shinlee.repository

import com.shinlee.network.Result
import com.shinlee.repository.model.MarvelCharacter

interface MarvelRepository {
    suspend fun getCharacters(
        apiKey: String,
        timestamp: String,
        hash: String
    ): Result<List<MarvelCharacter>>

}