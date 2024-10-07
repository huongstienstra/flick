package com.shinlee.showplus

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.local.pref.SharedPreferencesDataSource
import com.shinlee.network.ApiResult
import com.shinlee.repository.MarvelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarvelViewModel(
    private val repository: MarvelRepository,
    private val sharePreference: SharedPreferencesDataSource
) : ViewModel() {

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()




    fun getCharacters(apiKey: String, timeStamp: String, hash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCharacters(
                apiKey, timeStamp, hash
            )


            if(response is ApiResult.Success) {
                Log.e("API", "GET ${response.data}")
            }

            if(response is ApiResult.Error) {
                Log.e("API", "vmGET ${response.throwable.message}")
            }

        }
    }

}