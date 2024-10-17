package com.shinlee.showplus.ui.screens.authentication.signup.phone_number

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.ApiResult
import com.shinlee.repository.AuthenticationRepository
import com.shinlee.repository.model.UserInfo
import kotlinx.coroutines.flow.update

data class PhoneUiState(
    val selectedCountryCode: String = "+82", // default country code Korean
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val errorMessage: String? = null,

    val completedLogin: Boolean = false,
)

class PhoneNumberViewModel(
    application: Application, private val repository: AuthenticationRepository,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PhoneUiState())
    val uiState: StateFlow<PhoneUiState> = _uiState.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private var userInfo: UserInfo? = null

    private val gson = Gson()

    fun loadCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getApplication<Application>().assets
                    .open("flags.json")
                    .bufferedReader()
                    .use { it.readText() }

                val listType = object : TypeToken<List<Country>>() {}.type
                val countriesList: List<Country> = gson.fromJson(jsonString, listType)

                _countries.value = countriesList
            } catch (e: Exception) {
                // Handle error (e.g., log it or update an error state)
                e.printStackTrace()
            }
        }
    }

    fun onLoginPhoneNumber(idToken: String) {
        viewModelScope.launch {
            val result = repository.loginWithPhone(
                idToken = idToken,
                firebaseToken = "12"
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
                        phoneNumberError = result.throwable.message
                    )
                }
            }
        }
    }


    fun updatePhoneNumber(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                phoneNumberError = validatePhoneNumber(phone)
            )
        }
    }

    fun updateCountryCode(code: String) {
        _uiState.update {
            it.copy(selectedCountryCode = code)
        }
    }

    fun getPhoneNumber(): String {
        val phoneInput = _uiState.value.phoneNumber
        val countryCode = _uiState.value.selectedCountryCode

        val digitsOnly = phoneInput.replace(Regex("\\D"), "")

        // If the input starts with the country code (with or without '+'), remove it
        val phoneWithoutCountryCode = if (digitsOnly.startsWith(countryCode.removePrefix("+"))) {
            digitsOnly.substring(countryCode.removePrefix("+").length)
        } else {
            digitsOnly
        }

        // If the phone starts with '0', remove it
        val phoneWithoutLeadingZero = phoneWithoutCountryCode.removePrefix("0")

        // Combine the country code and the phone number
        return countryCode + phoneWithoutLeadingZero
    }

    fun resetPhoneNumber() {
        _uiState.update {
            it.copy(
                phoneNumber = "", phoneNumberError = null
            )
        }
    }

    private fun validatePhoneNumber(phone: String): String? {
        return when {
            phone.isEmpty() -> "Phone number cannot be empty"
            phone.length < 10 -> "Phone number is too short"
            phone.length > 15 -> "Phone number is too long"
            !phone.all { it.isDigit() } -> "Phone number should only contain digits"
            !isValidPhoneNumberFormat(phone) -> "Invalid phone number format"
            else -> null
        }
    }

    private fun isValidPhoneNumberFormat(phone: String): Boolean {
        val phoneRegex = """^(\+\d{1,3}[- ]?)?\d{10,14}$""".toRegex()
        return phone.matches(phoneRegex)
    }

    fun setError(error: String) {
        _uiState.update { it.copy(errorMessage = error) }

    }

    fun isLoggedIn(): Boolean {
        return userInfo?.profile?.isNotEmpty() == true
    }
}