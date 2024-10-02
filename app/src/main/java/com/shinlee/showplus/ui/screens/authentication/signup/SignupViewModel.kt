package com.shinlee.showplus.ui.screens.authentication.signup


import androidx.lifecycle.ViewModel
import com.nativemobilebits.loginflow.data.rules.Validator.validateConfirmPassword
import com.nativemobilebits.loginflow.data.rules.Validator.validateEmail
import com.nativemobilebits.loginflow.data.rules.Validator.validatePassword
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = "",
    val passwordError: String? = "",
    val confirmPasswordError: String? = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isActive: Boolean = false
)

class SignupViewModel : ViewModel() {

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
                confirmPasswordError = validateConfirmPassword(password = it.password, confirmPassword =  confirmPassword)
            )
        }
    }
}