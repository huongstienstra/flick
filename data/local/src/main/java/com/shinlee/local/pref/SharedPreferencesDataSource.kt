package com.shinlee.local.pref

interface SharedPreferencesDataSource {
    suspend fun setToken(token: String): Boolean
    suspend fun getToken(withBearPrefix: Boolean): String?
    suspend fun removeToken(): Boolean
}