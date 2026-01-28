package com.flick.app.ui.screens.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import androidx.recyclerview.widget.RecyclerView
import com.flick.network.ApiResult
import com.flick.repository.VideoRepository
import com.flick.app.paging_source.CommentPagingSource
import com.flick.app.paging_source.VideoPagingSource
import com.flick.app.ui.screens.comment.CommentData
import com.flick.app.ui.screens.show.core.video.ExoPlayerCache
import com.flick.app.ui.screens.show.core.video.VideoPreloader
import com.flick.app.ui.screens.show.mapping.toCommentData
import com.flick.app.ui.screens.show.mapping.toCommentDataList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ShowViewModel(
    private val playerCache: ExoPlayerCache,
    private val videoPreloader: VideoPreloader,
    private val repository: VideoRepository
) : ViewModel() {

    var currentPlayingPosition: Int = RecyclerView.NO_POSITION
    var currentPlaybackPosition: Long = 0

    fun getPlayer() = playerCache.getPlayer()
    fun releasePlayer(player: ExoPlayer) = playerCache.releasePlayer(player)

    /**
     * Gets a player for the given video URL.
     * If the video was preloaded, returns the preloaded player for instant playback.
     * Otherwise, returns a new player from the cache.
     */
    fun getPlayerForVideo(videoUrl: String): ExoPlayer {
        val preloadedPlayer = videoPreloader.getPreloadedPlayer(videoUrl)
        return if (preloadedPlayer != null) {
            Timber.d("ShowViewModel: Using preloaded player for: $videoUrl")
            preloadedPlayer
        } else {
            Timber.d("ShowViewModel: Getting new player for: $videoUrl")
            playerCache.getPlayer()
        }
    }

    /**
     * Preloads the next video for instant playback on swipe.
     * Call this when the user settles on a page.
     */
    fun preloadNextVideo(videoUrl: String) {
        Timber.d("ShowViewModel: Preloading next video: $videoUrl")
        videoPreloader.preloadNext(videoUrl)
    }

    /**
     * Checks if a video is preloaded and ready for instant playback.
     */
    fun isVideoPreloaded(videoUrl: String): Boolean {
        return videoPreloader.isPreloaded(videoUrl)
    }

    private val _isLikeVideo = MutableLiveData<Boolean>()
    val isLikeVideo: LiveData<Boolean> = _isLikeVideo

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _newCommentState = MutableStateFlow<CommentData?>(null)
    val newCommentState: StateFlow<CommentData?> = _newCommentState.asStateFlow()

    private val _shouldScrollToTop = MutableStateFlow(true)
    val shouldScrollToTop: StateFlow<Boolean> = _shouldScrollToTop.asStateFlow()

    var currentVideoId: Long = -1

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

    fun getCommentsPagingData(videoId: Long): Flow<PagingData<CommentData>> {
        currentVideoId = videoId
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                maxSize = 20
            ),
            pagingSourceFactory = {
                CommentPagingSource(repository, videoId)
            }
        ).flow
            .cachedIn(viewModelScope)
    }

    fun postComment(content: String) {
        viewModelScope.launch {
            val result = repository.postComment(currentVideoId, content)
            if (result is ApiResult.Success) {
                val newComment = result.data?.toCommentData()
                if (newComment != null) {
                    _newCommentState.value = newComment
                    _shouldScrollToTop.value = true
                }

            } else if (result is ApiResult.Error) {
                _errorMessage.postValue(result.throwable.message)
            }
        }
    }

    fun resetScrollState() {
        _shouldScrollToTop.value = false
    }

    fun likeVideo(videoId: Long) {
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
        videoPreloader.clear()
        playerCache.releaseAllPlayers()
    }

    fun releaseAllPlayers() {
        videoPreloader.clear()
        playerCache.releaseAllPlayers()
    }
}