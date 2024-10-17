package com.shinlee.showplus.ui.screens.authentication.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.ApiResult
import com.shinlee.repository.AuthenticationRepository
import com.shinlee.repository.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignupUiState(
    val isLoading: Boolean = false,

    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickName: String = "",
    val invitedCode: String? = null,

    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nickNameError: String? = null,
    val phoneNumberError: String? = null,

    val completedNickName: Boolean = false,
    val completedLogin: Boolean = false,

    val errorMessage: String? = null
)


class LoginViewModel(
    private val repository: AuthenticationRepository,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : ViewModel() {

    private var userInfo: UserInfo? = null

    private val _uiState = MutableStateFlow(
        SignupUiState(
            emailError = "",
            passwordError = "",
            confirmPasswordError = "",
            nickNameError = "",
        )
    )
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<AuthenticationFlowType>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }


    fun updatePassword(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                passwordError = if (password.length < 8) "Password must be at least 8 characters" else null
            )
        }
        validateConfirmPassword()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = if (confirmPassword != currentState.password) "Passwords do not match" else null
            )
        }
    }

    private fun validateConfirmPassword() {
        val currentState = _uiState.value
        if (currentState.confirmPassword.isNotEmpty()) {
            _uiState.update { state ->
                state.copy(
                    confirmPasswordError = if (currentState.confirmPassword != currentState.password) "Passwords do not match" else null
                )
            }
        }
    }

    fun isPasswordValid(isCheckConfirmPassword: Boolean): Boolean {
        if (isCheckConfirmPassword) {
            val currentState = _uiState.value
            return currentState.password.length >= 8 &&
                    currentState.password == currentState.confirmPassword &&
                    currentState.passwordError == null &&
                    currentState.confirmPasswordError == null
        } else {
            val currentState = _uiState.value
            return currentState.password.length >= 8 && currentState.passwordError == null
        }
    }

    fun updateNickName(nickName: String) {
        _uiState.update {
            it.copy(
                nickName = nickName,
                nickNameError = validateNickName(nickName = nickName)
            )
        }
    }

    fun updateInvitedCode(code: String) {
        _uiState.update {
            it.copy(
                invitedCode = code,
            )
        }
    }


    fun resetErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }


    fun onLoginEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loginWithEmail(
                email = _uiState.value.email,
                password = _uiState.value.password,
                passwordConfirm = _uiState.value.confirmPassword,
                firebaseToken = "1"
            )
            if (result is ApiResult.Success) {
                val data = result.data
                userInfo = data.userInfo
                //save token
                sharedPreferencesDataSource.setToken(data.token)
                //save user info to shared preferences
                val gson = Gson()
                val userInfoJson = gson.toJson(userInfo)
                sharedPreferencesDataSource.saveUserInformation(userInfoJson)

                _uiState.update { it.copy(completedLogin = true) }
            } else if (result is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        completedLogin = false,
                        passwordError = result.throwable.message
                    )
                }
            }
        }
    }

    fun submitNickNameAndInviteCode() {
        val nickName = _uiState.value.nickName
        val inviteCode = _uiState.value.invitedCode ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.submitNickNameAndInviteCode(
                nickName, inviteCode
            )

            if (result is ApiResult.Success) {
                val data = result.data
                userInfo = data.userInfo

                //save user info to shared preferences
                val gson = Gson()
                val userInfoJson = gson.toJson(userInfo)
                sharedPreferencesDataSource.saveUserInformation(userInfoJson)

                _uiState.update { it.copy(completedNickName = true) }
            } else if (result is ApiResult.Error) {
                _uiState.update {
                    it.copy(
                        completedNickName = false,
                        nickNameError = result.throwable.message
                    )
                }
            }
        }

    }

    fun isLoggedIn(): Boolean {
        return userInfo?.profile?.isNotEmpty() == true
    }

    fun identifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                repository.checkEmailExist(_uiState.value.email)
            if (result is ApiResult.Success) {
                if (result.data.isExist) { // already login
                    _navigationEvents.send(AuthenticationFlowType.LOGIN)
                } else { // first time sign up
                    _navigationEvents.send(AuthenticationFlowType.SIGN_UP)
                }
            } else if (result is ApiResult.Error) {
                _uiState.update { it.copy(emailError = result.throwable.message) }
            }
        }
    }

    fun resetPasswordError() {
        _uiState.update {
            it.copy(
                passwordError = null,
                confirmPasswordError = null
            )
        }
    }

    private fun validateNickName(nickName: String): String? {
        return when {
            nickName.isEmpty() -> "nick name not be empty"
            else -> null
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
}