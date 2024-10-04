package com.shinlee.showplus.ui.screens.authentication.login.v2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.Result
import com.shinlee.network.model.LoginRequest
import com.shinlee.repository.AuthenticationRepository
import com.shinlee.showplus.ui.screens.authentication.login.LoginData
import com.shinlee.showplus.ui.screens.show.mapping.toLoginData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = "",
    val passwordError: String? = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean? = null,
)

class LoginViewModelV2(
    private val repository: AuthenticationRepository,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
    ) : ViewModel() {

    var token = MutableStateFlow("")
    var isPhoneValid = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
    }

    fun loginByEmail() {
        val currentState = _uiState.value
        if (currentState.emailError == null && currentState.passwordError == null) {
            _uiState.update { it.copy(isLoading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.loginByEmail(LoginRequest(currentState.email.trim(), currentState.password.trim()))
                if (result is Result.Success) {
                    val data = result.data
                    if (result.data.userInfo.phone.isEmpty()){
                        isPhoneValid.value = false
                        token.value = data.token
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    }else {
                        saveUserInfo(result.data.toLoginData().userInfo)
                        sharedPreferencesDataSource.setToken(data.token)
                        isPhoneValid.value = true
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    }
                } else if (result is Result.Error) {
                    Log.e("login", "Error: ${result.throwable.message}")

                    // Handle later
                    _uiState.update { it.copy(isLoading = false, passwordError = "Your password is not correct") }
                }
            }
        }
    }


    private fun saveUserInfo(userInfo: LoginData.UserInfo){
        viewModelScope.launch(Dispatchers.IO) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            ObjectOutputStream(byteArrayOutputStream).use { oos ->
                oos.writeObject(userInfo) // Serialize the object
            }
            val byteArray = byteArrayOutputStream.toByteArray()
            sharedPreferencesDataSource.saveUserInformation(byteArray.joinToString {","})
        }
    }

    private fun validateEmail(email: String): String? {
        return if (email.isEmpty()) {
            "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Please enter valid email address"
        } else {
            null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
//            !password.any { it.isDigit() } -> "Password must contain at least one number"
//            !password.any { it.isLetter() } -> "Password must contain at least one letter"
            else -> null
        }
    }

    fun refreshState(){
        _uiState.update {it.copy(isLoggedIn = null)}
    }

}