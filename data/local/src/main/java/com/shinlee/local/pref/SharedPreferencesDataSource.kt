package com.shinlee.local.pref


interface SharedPreferencesDataSource {
    suspend fun setToken(token: String): Boolean
    suspend fun getToken(withBearPrefix: Boolean): String
    suspend fun removeToken(): Boolean
    suspend fun saveUserInformation(userInfo: String): Boolean
    suspend fun getUserInformation(): String

}