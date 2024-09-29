package com.shinlee.local.pref

import android.content.SharedPreferences

class SharedPreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {

    override suspend fun setToken(token: String): Boolean {
        return sharedPreferences.edit().putString(KEY_TOKEN, token).commit()
    }

    override suspend fun getToken(withBearPrefix: Boolean): String? {
        var token = sharedPreferences.getString(KEY_TOKEN, "")
        if (!token.isNullOrEmpty() && withBearPrefix) {
            token = "HEADER_VALUE_AUTHORIZATION_PREFIX$token"
        }
        return token
    }

    override suspend fun removeToken(): Boolean {
        return sharedPreferences.edit().remove(KEY_TOKEN).commit()
    }

    companion object {
        private const val KEY_TOKEN = "key_token"
    }
}