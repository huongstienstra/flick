package com.shinlee.showplus.ui.screens.show

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.network.Result
import com.shinlee.repository.VideoRepository
import com.shinlee.showplus.ui.screens.show.core.video.ExoPlayerCache
import com.shinlee.showplus.ui.screens.show.mapping.toVideoShowList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowViewModelV2(
    private val playerCache: ExoPlayerCache,
    private val repository: VideoRepository
) : ViewModel() {

    var currentPlayingPosition: Int = RecyclerView.NO_POSITION
    var currentPlaybackPosition: Long = 0

    fun getPlayer() = playerCache.getPlayer()
    fun releasePlayer(player: ExoPlayer) = playerCache.releasePlayer(player)

    private val _videos = MutableLiveData<List<VideoShow>>(emptyList())
    val videos: LiveData<List<VideoShow>> = _videos

    init {
        getVideos()
    }

    private fun getVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getVideos()
            if (result is Result.Success) {
                val data = result.data.toVideoShowList()
                _videos.postValue(data)// Update the StateFlow with the result
            } else if (result is Result.Error) {
                Log.e("getVideos", "Error: ${result.throwable.message}")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        playerCache.releaseAllPlayers()
    }

    fun releaseAllPlayers() {
        playerCache.releaseAllPlayers()
    }
}