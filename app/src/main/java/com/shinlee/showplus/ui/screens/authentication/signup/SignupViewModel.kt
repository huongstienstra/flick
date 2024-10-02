package com.shinlee.showplus.ui.screens.authentication.signup


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativemobilebits.loginflow.data.rules.Validator.validateConfirmPassword
import com.nativemobilebits.loginflow.data.rules.Validator.validateEmail
import com.nativemobilebits.loginflow.data.rules.Validator.validatePassword
import com.shinlee.network.Result
import com.shinlee.network.model.RegisterRequest
import com.shinlee.repository.AuthenticationRepository
import com.shinlee.showplus.ui.screens.show.mapping.toLoginData
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
    val emailError: String? = "",
    val passwordError: String? = "",
    val confirmPasswordError: String? = "",
    val isLoading: Boolean = false,
    val isSignUpSuccess: Boolean = false,
)

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

    private fun validateEmail(email: String): String? {
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
}