package com.shinlee.showplus.ui.screens.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool

class MainViewModelFactory(
    private val playersPool: PlayersPool
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowViewModel::class.java)) {
            return ShowViewModel(playersPool) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}