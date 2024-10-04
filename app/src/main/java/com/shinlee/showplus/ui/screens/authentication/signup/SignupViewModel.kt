package com.shinlee.showplus.ui.screens.authentication.signup


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.network.Result
import com.shinlee.network.model.CheckEmailExistRequest
import com.shinlee.network.model.RegisterRequest
import com.shinlee.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignupUiState(
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
    val isLoading: Boolean = false,
    val isSignUpSuccess: Boolean? = null,
    val isCheckEmailExist: Boolean? = null
)

data class PhonePrefix(val country: String, val code: String)

class SignupViewModel(
    private val repository: AuthenticationRepository,
) : ViewModel() {

    var token = MutableStateFlow("")
    var emailState = MutableStateFlow("")

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
        emailState.value = email
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

    fun validateEmail(email: String): String? {
        return if (email.isEmpty()) {
            "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Please enter valid email address"
        } else {
            null
        }
    }

    fun registerFirstStep() {
        val currentState = _uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.registerByEmail(
                RegisterRequest(
                    email = emailState.value,
                    password = currentState.password.trim(),
                    password_confirm = currentState.confirmPassword.trim()
                )
            )
            if (result is Result.Success) {
                val data = result.data
                token.value = data.token
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
        val currentState = _uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                repository.checkEmailExist(CheckEmailExistRequest(email = currentState.email))
            if (result is Result.Success) {
                if (result.data.isExist) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            emailError = "Your Email have Exist",
                            isCheckEmailExist = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, isCheckEmailExist = false) }
                }
            } else if (result is Result.Error) {
                Log.e("checkEmailExist", "Error: ${result.throwable.message}")
            }
        }
    }

    fun refreshState(){
        _uiState.update {it.copy(isSignUpSuccess = null, isCheckEmailExist = null)}
    }
}