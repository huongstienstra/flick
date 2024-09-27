package com.shinlee.showplus.ui.screens.authentication.datasource

import kotlinx.coroutines.flow.Flow

interface SharedPreferencesDataSource {
    suspend fun setToken(token: String): Boolean
    suspend fun getToken(withBearPrefix: Boolean = true): String?
    suspend fun removeToken(): Boolean
}