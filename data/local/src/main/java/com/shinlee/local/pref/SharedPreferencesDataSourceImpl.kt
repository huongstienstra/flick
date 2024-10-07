package com.shinlee.local.pref

import android.content.SharedPreferences
import com.google.gson.Gson


class SharedPreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {

    override fun setToken(token: String): Boolean {
        return sharedPreferences.edit().putString(KEY_TOKEN, token).commit()
    }

    override fun getToken(): String {
        return sharedPreferences.getString(KEY_TOKEN, "") ?: ""
    }

    override fun removeToken(): Boolean {
        return sharedPreferences.edit().remove(KEY_TOKEN).commit()
    }

    override fun isUserLoggedIn(): Boolean {
        return !sharedPreferences.getString(KEY_TOKEN, "").isNullOrEmpty()
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