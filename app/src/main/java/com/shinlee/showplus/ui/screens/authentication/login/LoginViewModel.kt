package com.shinlee.showplus.ui.screens.authentication.login

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.nativemobilebits.loginflow.data.rules.Validator
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
import com.nativemobilebits.loginflow.navigation.Screen
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.repository.MarvelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: MarvelRepository,
    private val sharePreference: SharedPreferencesDataSource
) : ViewModel() {

    val signUpPageClick = MutableSharedFlow<Unit>()

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }

            is LoginUIEvent.LoginWithGoogleClicked -> {
                loginByGoogle()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun loginByGoogle() {

    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            loginInProgress.value = true
            val email = loginUIState.value.email
            val password = loginUIState.value.password

            if (email == "khanh@gmail.com" && password == "123456") {
                PostOfficeAppRouter.navigateTo(Screen.GoToMainScreen)
            }
        }
    }
}