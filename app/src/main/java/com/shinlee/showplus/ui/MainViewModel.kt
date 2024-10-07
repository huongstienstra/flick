package com.shinlee.showplus.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.repository.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class LoginState {
    data object Unknown : LoginState()
    data object LoggedIn : LoginState()
    data object LoggedOut : LoginState()
    data object IncompleteProfile : LoginState()
    data object TokenExpired : LoginState()
}

class MainViewModel(private val sharedPreferencesDataSource: SharedPreferencesDataSource) :
    ViewModel() {


    private val _loginState = MutableStateFlow<LoginState>(LoginState.Unknown)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

     fun checkLoginStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val gson = Gson()
            val userInfoJson = sharedPreferencesDataSource.getUserInformation()
            val token = sharedPreferencesDataSource.getToken()
            val userInfo = try {
                userInfoJson.let { gson.fromJson(it, UserInfo::class.java) }
            } catch (e: JsonSyntaxException) {
                Log.e("login","$e")
                null
            }
            Log.e("login","$userInfoJson and $userInfo and $token")
            if (userInfo != null && userInfo.profile.isNotEmpty() && token.isNotEmpty()) {
                _loginState.value = LoginState.LoggedIn
            } else {
                Log.e("login","IncompleteProfile")
                _loginState.value = LoginState.IncompleteProfile
            }

        }
    }

}