package com.shinlee.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.shinlee.common.composable.core.PlayerSurface
import com.shinlee.common.composable.core.SURFACE_TYPE_SURFACE_VIEW
@Composable
fun VideoPlayer(
    modifier: Modifier,
    videoUri: String,
    thumbnailUrl: String,
    pagerState: PagerState,
    pageIndex: Int
) {
        Surface(modifier = modifier.fillMaxSize()) {
            Box(
                Modifier.fillMaxSize()
            ) {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                if(pagerState.settledPage == pageIndex) {
                    val exoPlayer = remember(context) {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(videoUri))
                            repeatMode = Player.REPEAT_MODE_ONE
                            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                            prepare()
                            // playWhenReady = true
                        }
                    }
//                // Auto-play video if it's the currently visible page
//                LaunchedEffect(pagerState.currentPage == pageIndex) {
//                    if (pagerState.currentPage == pageIndex) {
//                        exoPlayer.playWhenReady = true
//                    } else {
//                        exoPlayer.playWhenReady = false
//                        exoPlayer.stop()
//                    }
//                }

                    PlayerSurface(
                        player = exoPlayer,
                        surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                        //modifier = Modifier
                    )

                    // Handle ExoPlayer lifecycle
                    DisposableEffect(lifecycleOwner) {
                        val lifecycleObserver = LifecycleEventObserver { _, event ->
                            when (event) {
                                Lifecycle.Event.ON_PAUSE -> {
                                    exoPlayer.pause()
                                }

                                Lifecycle.Event.ON_RESUME -> {
                                    exoPlayer.playWhenReady = true
                                }

                                Lifecycle.Event.ON_DESTROY -> {
                                    exoPlayer.release()
                                }

                                else -> {}
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                        }
                    }
                }



            }
        }

}



