package com.shinlee.showplus

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.network.Result
import com.shinlee.repository.MarvelRepository
import com.shinlee.showplus.ui.screens.authentication.datasource.SharedPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MarvelViewModel(
    private val repository: MarvelRepository,
    private val sharePreference: SharedPreferencesDataSource
) : ViewModel() {

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()


     fun checkForActiveSession(): Boolean {
         return false
     }

    fun getCharacters(apiKey: String, timeStamp: String, hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCharacters(
                apiKey, timeStamp, hash
            )


            if(response is Result.Success) {
                Log.e("API", "GET ${response.data}")
            }

            if(response is Result.Error) {
                Log.e("API", "vmGET ${response.throwable.message}")
            }

        }
    }

}