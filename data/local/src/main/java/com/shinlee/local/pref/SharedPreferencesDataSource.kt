package com.shinlee.local.pref


interface SharedPreferencesDataSource {
    fun setToken(token: String): Boolean
    fun getToken(): String
    fun removeToken(): Boolean
    fun isUserLoggedIn(): Boolean
    suspend fun saveUserInformation(userInfo: String): Boolean
    suspend fun getUserInformation(): String

}