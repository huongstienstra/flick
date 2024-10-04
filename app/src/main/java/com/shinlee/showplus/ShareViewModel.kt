package com.shinlee.showplus

import androidx.lifecycle.ViewModel
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.showplus.ui.screens.authentication.login.LoginData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class ShareViewModel(
    private val sharePreference: SharedPreferencesDataSource
) : ViewModel() {

    private val userInfo = MutableStateFlow(LoginData.UserInfo())

    fun checkForActiveSession(): Boolean {
        return runBlocking {
             sharePreference.getToken(true).isNotEmpty()
        }
    }

    fun getDataUserInfo() {
        runBlocking {
            val userInfoString = sharePreference.getUserInformation()
            val byteArray = userInfoString.split(",").map { it.toByte() }.toByteArray()
            val byteArrayInputStream = ByteArrayInputStream(byteArray)
            ObjectInputStream(byteArrayInputStream).use { ois ->
                userInfo.value =  ois.readObject() as LoginData.UserInfo
            }
         }
    }
}