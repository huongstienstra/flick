package com.shinlee.showplus.ui.screens.show

import androidx.lifecycle.ViewModel
import com.shinlee.showplus.ui.screens.show.core.video.PlayersAction
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class ShowViewModel(
     val playersPool: PlayersPool
) : ViewModel() {
    val videoUrls: Flow<List<String>> = flowOf(
        listOf(
            "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/d42f5093-6017-49df-822b-0caa9f3582ce",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4"
        )
    )
    private val _playbackPositions: MutableStateFlow<List<Long>> = MutableStateFlow(
        listOf(0L, 0L, 0L, 0L, 0L, 0L, 0L)
    )
    val playbackPositions: Flow<List<Long>> = _playbackPositions

    private val _playerActions: MutableSharedFlow<PlayersAction> = MutableSharedFlow(extraBufferCapacity = 1)
    val playersActions: Flow<PlayersAction> = _playerActions

    fun updatePlaybackPosition(index: Int, playbackPosition: Long) {
        _playbackPositions.update { playbackPositions ->
//      if (index in playbackPositions.indices) { // Check if index is valid
//        playbackPositions.toMutableList().apply {
//          removeAt(index)
//          add(index, playbackPosition)
//        }
//      } else {
//        playbackPositions // Return the original list if index is invalid
//      }
            playbackPositions
        }
    }

    fun releasePlayers() {
        _playerActions.tryEmit(PlayersAction.RELEASE)
        playersPool.releaseAll()
    }

    fun restartPlayers() {
        _playerActions.tryEmit(PlayersAction.RESTART)
    }

}