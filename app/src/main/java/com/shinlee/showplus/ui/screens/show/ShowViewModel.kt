package com.shinlee.showplus.ui.screens.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.network.ApiResult
import com.shinlee.repository.VideoRepository
import com.shinlee.repository.VideoShow
import com.shinlee.repository.model.VideoInfo
import com.shinlee.repository.paging_source.VideoPagingSource
import com.shinlee.showplus.ui.screens.show.core.video.ExoPlayerCache
import com.shinlee.showplus.ui.screens.show.mapping.toVideoShowList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ShowViewModel(
    private val playerCache: ExoPlayerCache,
    private val repository: VideoRepository
) : ViewModel() {

    var currentPlayingPosition: Int = RecyclerView.NO_POSITION
    var currentPlaybackPosition: Long = 0

    fun getPlayer() = playerCache.getPlayer()
    fun releasePlayer(player: ExoPlayer) = playerCache.releasePlayer(player)

    val videos: Flow<PagingData<VideoShow>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 5,
            enablePlaceholders = false,
            maxSize = 30
        ),
        pagingSourceFactory = { VideoPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)


    override fun onCleared() {
        super.onCleared()
        playerCache.releaseAllPlayers()
    }

    fun releaseAllPlayers() {
        playerCache.releaseAllPlayers()
    }
}