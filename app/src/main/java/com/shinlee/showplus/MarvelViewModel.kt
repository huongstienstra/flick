package com.shinlee.showplus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinlee.network.Result
import com.shinlee.repository.MarvelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarvelViewModel(
    private val repository: MarvelRepository
) : ViewModel() {

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