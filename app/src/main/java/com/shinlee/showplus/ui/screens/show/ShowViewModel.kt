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

    val video1 = VideoShow(
        id = "55",  // Extracted from JSON
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/d2c9c5d9-5141-499d-945f-35b6c6d70dc3",  // Extracted from JSON
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/d42f5093-6017-49df-822b-0caa9f3582ce"  // Extracted from JSON
    )

    val video2 = VideoShow(
        id = "56",  // Extracted from JSON
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/acf38c62-cc2c-442f-ac1a-df520bcb7245",  // Extracted from JSON
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/674d9e9a-82b3-4fad-983f-3308a276356b"  // Extracted from JSON
    )

    val video3 = VideoShow(
        id = "57",  // Extracted from JSON
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/1a70d750-2462-49d9-8413-0251d10aed17",  // Extracted from JSON
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/6ccbb6f3-7ecb-4619-888e-8c6566be6e4e"  // Extracted from JSON
    )

    val video4 = VideoShow(
        id = "58",  // Extracted from JSON
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/9007347a-458e-447a-8510-25c378241ea7",  // Extracted from JSON
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/25c46d18-be47-4556-b5b4-621bad5a4cf1"  // Extracted from JSON
    )

    val video5 = VideoShow(
        id = "59",  // Extracted from JSON
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/77e0beb1-0221-4943-8df6-07314d8e985b",  // Extracted from JSON
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/725758c1-8634-42d7-b8c6-9fda6abd53a7"  // Extracted from JSON
    )

    val video6 = VideoShow(
        id = "60",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/586c4d1f-df7e-440d-ae55-4e486662f8ac",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/b8018bcb-c832-4ae1-879e-64aad1b9a495"
    )

    val video7 = VideoShow(
        id = "61",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/d3f904b5-6ac3-4b77-8c30-649c86842d67",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/f123b618-d884-4b9a-a25f-2db4b12a4133"
    )

    val video8 = VideoShow(
        id = "62",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/34f74048-1db1-4463-811d-5a4fc8592f32",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/ddba0b39-1703-4963-9569-f469fcae9e3f"
    )

    val video10 = VideoShow(
        id = "64",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/2dbefd55-bb8d-4c92-afc4-b3f1373ddd02",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/9d6d8454-401b-43f2-a2ce-dee3ae9d9515"
    )

    val video11 = VideoShow(
        id = "65",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/5db92f3a-69f0-4f55-b6ba-2fea6839b4b3",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/3ae11009-7353-49d6-bf63-1bc76d46e985"
    )

    val video12 = VideoShow(
        id = "66",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/e3126622-5cde-4a10-94c6-44b6aa2d6a3f",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/30c7face-6945-4c7a-9897-f958246ba8c9"
    )

    val video13 = VideoShow(
        id = "67",
        thumbnail = "https://showplusv2.s3-accelerate.amazonaws.com/showplusv2/contest/0b121a9e-01fd-493d-8689-3466d4ce0719",
        videoLink = "https://d2nuf14k1mlj0o.cloudfront.net/showplusv2/contest/64ef9349-7652-4a0a-a0f0-24eede228f3d"
    )


    // Create a list of Video objects
    val videoList = listOf(video1, video2, video3, video4, video5, video6, video7, video8, video10, video11, video12, video13)

    val videoUrls: Flow<List<VideoShow>> = flowOf(
        videoList
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