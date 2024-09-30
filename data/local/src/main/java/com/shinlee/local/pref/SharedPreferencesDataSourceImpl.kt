package com.shinlee.local.pref

import android.content.SharedPreferences


class SharedPreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {


    override suspend fun setToken(token: String): Boolean {
        return sharedPreferences.edit().putString(KEY_TOKEN, token).commit()
    }

    override suspend fun getToken(withBearPrefix: Boolean): String {
        var token = sharedPreferences.getString(KEY_TOKEN, "") ?: ""
        if (token.isNotEmpty() && withBearPrefix) {
            token = "HEADER_VALUE_AUTHORIZATION_PREFIX$token"
        }
        return token
    }

    override suspend fun removeToken(): Boolean {
        return sharedPreferences.edit().remove(KEY_TOKEN).commit()
    }

    override suspend fun saveUserInformation(userInfo: String): Boolean {
        return sharedPreferences.edit().putString(KEY_USER_INFO, userInfo).commit()
    }

    override suspend fun getUserInformation(): String {
        return sharedPreferences.getString(KEY_USER_INFO, "") ?: ""
    }

    companion object {
        private const val KEY_TOKEN = "key_token"
        private const val KEY_USER_INFO = "key_user_info"
    }
}