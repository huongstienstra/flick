package com.shinlee.showplus.ui.screens.authentication.datasource

import com.shinlee.showplus.ShowPlusApplication


private const val KEY_TOKEN = "token"

class SharedPreferencesDataSourceImplement : SharedPreferencesDataSource {
    override suspend fun setToken(token: String): Boolean {
        return ShowPlusApplication.instance.sharedPreferences.edit().putString(KEY_TOKEN, token).commit()

    }

    override suspend fun getToken(withBearPrefix: Boolean): String? {
            var token = ShowPlusApplication.instance.sharedPreferences.getString(KEY_TOKEN, "")
            if((!token.isNullOrEmpty()) && withBearPrefix) {
                token = "HEADER_VALUE_AUTHORIZATION_PREFIX" + token
            }
            return token

    }

    override suspend fun removeToken(): Boolean {
        return ShowPlusApplication.instance.sharedPreferences.edit().remove(KEY_TOKEN).commit()
    }
}