package com.shinlee.showplus.ui.screens.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.network.ApiResult
import com.shinlee.repository.VideoRepository
import com.shinlee.showplus.paging_source.CommentPagingSource
import com.shinlee.showplus.paging_source.VideoPagingSource
import com.shinlee.showplus.ui.screens.comment.CommentData
import com.shinlee.showplus.ui.screens.show.core.video.ExoPlayerCache
import com.shinlee.showplus.ui.screens.show.mapping.toCommentDataList
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

    private val _isLikeVideo = MutableLiveData<Boolean>()
    val isLikeVideo: LiveData<Boolean> = _isLikeVideo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var currentVideoId: Int? = null

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

    fun getCommentsPagingData(videoId: Int): Flow<PagingData<CommentData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                maxSize = 30
            ),
            pagingSourceFactory = { CommentPagingSource(repository, videoId) }
        ).flow.cachedIn(viewModelScope)
    }

    fun likeVideo(videoId: Int) {
        viewModelScope.launch {
            val result = repository.likeVideo(videoId)
            if (result is ApiResult.Success) {
                if (result.data) {
                    _isLikeVideo.postValue(result.data)
                }
            } else if (result is ApiResult.Error) {
                _errorMessage.postValue(result.throwable.message)
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