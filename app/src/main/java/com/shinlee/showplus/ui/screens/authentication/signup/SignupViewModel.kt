package com.shinlee.showplus.ui.screens.authentication.signup


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.Result
import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.RegisterRequest
import com.shinlee.repository.AuthenticationRepository
import com.shinlee.showplus.ui.screens.authentication.login.LoginData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

data class SignupUiState(
    val isLoading: Boolean = false,

    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickName: String = "",
    val phoneNumber: String = "",

    val emailError: String? = "",
    val passwordError: String? = "",
    val confirmPasswordError: String? = "",
    val nickNameError: String? = "",

    val phoneNumberError: String? = "",
    val isSignUpSuccess: Boolean? = null,
    val isEmailValid: Boolean? = null
)


class SignupViewModel(
    private val repository: AuthenticationRepository,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

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
                passwordError = validatePassword(password = it.password)
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = validateConfirmPassword(
                    password = it.password,
                    confirmPassword = confirmPassword
                )
            )
        }
    }

    fun updateNickName(nickName: String) {
        _uiState.update {
            it.copy(
                nickName = nickName,
                nickNameError = validateNickName(nickName = it.nickName)
            )
        }
    }

    fun updatePhoneNumber(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                phoneNumberError = validatePhoneNumber(phone = it.phoneNumber)
            )
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

    fun onSignup() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("signup", "${_uiState.value.email}")
            val result = repository.registerByEmail(
                email = _uiState.value.email,
                password = _uiState.value.password,
                passwordConfirm = _uiState.value.confirmPassword
            )
            if (result is Result.Success) {
                val data = result.data
                sharedPreferencesDataSource.setToken(data.token)
                _uiState.update { it.copy(isLoading = false, isSignUpSuccess = true) }
            } else if (result is Result.Error) {
                Log.e("register", "Error: ${result.throwable.message}")
            }
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

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            password != confirmPassword -> "Your password no correct"
            else -> null
        }
    }

    private fun validateNickName(nickName: String): String? {
        return when {
            nickName.isEmpty() -> "nick name not be empty"
            nickName.length < 2 -> "Nick name must be at least 3 characters"
            else -> null
        }
    }

    private fun validatePhoneNumber(phone: String): String? {
        return when {
            phone.isEmpty() -> "Phone can not be empty"
            else -> null
        }
    }

    fun checkEmailExit() {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                repository.checkEmailExist(_uiState.value.email)
            if (result is Result.Success) {
                if (result.data.isExist) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEmailValid = false
                        )
                    }
                } else {
                    Log.e("checkEmailExist", "${result.data.isExist}")
                    _uiState.update { it.copy(isEmailValid = true) }
                }
            } else if (result is Result.Error) {
                Log.e("checkEmailExist", "Error: ${result.throwable.message}")
            }
        }
    }

    fun checkedEmailAlready() {
        _uiState.update { it.copy(isEmailValid = null) }
    }
}