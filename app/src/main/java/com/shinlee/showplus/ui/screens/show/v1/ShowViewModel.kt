package com.shinlee.showplus.ui.screens.show.v1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.shinlee.network.ApiResult
import com.shinlee.repository.VideoRepository
import com.shinlee.showplus.ui.screens.show.VideoShow
import com.shinlee.showplus.ui.screens.show.core.video.PlayersAction
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import com.shinlee.showplus.ui.screens.show.mapping.toVideoShowList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShowViewModel(
    val repository: VideoRepository,
    val playersPool: PlayersPool
) : ViewModel() {
//    val videoUrls: Flow<List<String>> = flowOf(
//        listOf(
//            "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/d42f5093-6017-49df-822b-0caa9f3582ce",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
//            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4"
//        )
//    )

    val videoList = listOf(
        VideoShow(id=2, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/ba2da881-169f-4d9e-8c89-8e11118c5805/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/ba2da881-169f-4d9e-8c89-8e11118c5805.jpg"),
        VideoShow(id=3, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/a8822dfa-071f-4277-adf4-f750569975d8/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/a8822dfa-071f-4277-adf4-f750569975d8.jpg"),
        VideoShow(id=4, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/3ada368a-15b5-4359-88b3-c30b259d9302/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/3ada368a-15b5-4359-88b3-c30b259d9302.jpg"),
        VideoShow(id=5, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/caf4083c-6f5a-406b-99bb-1049781bbb4c/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/caf4083c-6f5a-406b-99bb-1049781bbb4c.jpg"),
        VideoShow(id=6, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/f8f53726-8d18-4f70-b7a7-9470e33c5bae/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/f8f53726-8d18-4f70-b7a7-9470e33c5bae.jpg"),
        VideoShow(id=7, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/6e0674ad-487c-46a6-9651-789619d6ccdc/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/6e0674ad-487c-46a6-9651-789619d6ccdc.jpg"),
        VideoShow(id=8, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/bc0eb253-a89d-45d6-bd0e-dc02dba42f1c/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/bc0eb253-a89d-45d6-bd0e-dc02dba42f1c.jpg"),
        VideoShow(id=9, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/1f5d0d19-b8ca-4de0-bb48-50ef75f97530/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/1f5d0d19-b8ca-4de0-bb48-50ef75f97530.jpg"),
        VideoShow(id=10, videoLink="https://d3h5z9773yj8xo.cloudfront.net/streams/3f45aed9-6980-4bed-ac37-ebbb162a2d87/video.m3u8", thumbnail="https://d3h5z9773yj8xo.cloudfront.net/thumbnails/3f45aed9-6980-4bed-ac37-ebbb162a2d87.jpg")
    )


    // Create a list of Video objects

    val videoUrls: Flow<List<VideoShow>> = flowOf(
        videoList
    )
    private val _playbackPositions: MutableStateFlow<List<Long>> = MutableStateFlow(
        listOf(0L, 0L, 0L, 0L, 0L, 0L, 0L)
    )
    val playbackPositions: Flow<List<Long>> = _playbackPositions

    private val _playerActions: MutableSharedFlow<PlayersAction> =
        MutableSharedFlow(extraBufferCapacity = 1)
    val playersActions: Flow<PlayersAction> = _playerActions

    private val _videoStateFlow = MutableStateFlow<List<VideoShow>>(emptyList())
    val videoStateFlow: StateFlow<List<VideoShow>> = _videoStateFlow

    fun getVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getVideos()
            if (result is ApiResult.Success) {
                val data = result.data.toVideoShowList()
                _videoStateFlow.value = data // Update the StateFlow with the result
            } else if (result is ApiResult.Error) {
                Log.e("getVideos", "Error: ${result.throwable.message}")
            }
        }
    }

    fun updatePlaybackPosition(index: Int, playbackPosition: Long) {
        _playbackPositions.update { playbackPositions ->
//      if (index in playbackPositions.indices) { // Check if index is valid
//        playbackPosiMtions.toMutableList().apply {
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