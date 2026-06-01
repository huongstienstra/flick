package com.flick.app.ui.screens.show.composable

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import timber.log.Timber

/**
 * Composable wrapper for ExoPlayer PlayerView
 *
 * @param videoUrl URL of the video to play
 * @param player ExoPlayer instance to use for playback
 * @param isPlaying Whether the video should be playing
 * @param onPlayerReady Callback when player is ready
 * @param modifier Modifier for the composable
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    thumbnailUrl: String?,
    player: ExoPlayer?,
    isPlaying: Boolean,
    onPlayerReady: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isMediaSet by remember(videoUrl) { mutableStateOf(false) }
    var showThumbnail by remember(videoUrl) { mutableStateOf(true) }

    // Set media item when player and URL are available
    LaunchedEffect(player, videoUrl, isPlaying) {
        isMediaSet = false
        showThumbnail = true
        player?.let { exoPlayer ->
            if (videoUrl.isNotBlank()) {
                try {
                    val currentUrl = exoPlayer.currentMediaItem
                        ?.localConfiguration
                        ?.uri
                        ?.toString()
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

                    if (currentUrl != videoUrl) {
                        val mediaItem = MediaItem.fromUri(videoUrl)
                        exoPlayer.stop()
                        exoPlayer.clearMediaItems()
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                    }

                    exoPlayer.playWhenReady = isPlaying
                    if (isPlaying) {
                        exoPlayer.play()
                        onPlayerReady()
                    }
                    isMediaSet = true
                    Timber.d("Media set: $videoUrl")
                } catch (e: Exception) {
                    Timber.e(e, "Error setting media item")
                }
            }
        }
    }

    // Control playback state
    LaunchedEffect(player, isPlaying, isMediaSet) {
        player?.let { exoPlayer ->
            if (isMediaSet) {
                if (isPlaying) {
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()
                    onPlayerReady()
                } else {
                    exoPlayer.pause()
                }
            }
        }
    }

    // Player listener for state changes
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                showThumbnail = false
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        Timber.d("Player STATE_READY")
                    }
                    Player.STATE_BUFFERING -> {
                        Timber.d("Player STATE_BUFFERING")
                    }
                    Player.STATE_ENDED -> {
                        Timber.d("Player STATE_ENDED")
                    }
                    Player.STATE_IDLE -> {
                        Timber.d("Player STATE_IDLE")
                    }
                }
            }
        }

        player?.addListener(listener)

        onDispose {
            player?.removeListener(listener)
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false
                    setKeepContentOnPlayerReset(false)
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            update = { playerView ->
                if (playerView.player !== player) {
                    playerView.player = player
                }
            }
        )

        if (showThumbnail) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * Simplified VideoPlayer that manages its own ExoPlayer instance
 */
@OptIn(UnstableApi::class)
@Composable
fun SimpleVideoPlayer(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(videoUrl) {
        if (videoUrl.isNotBlank()) {
            val mediaItem = MediaItem.fromUri(videoUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
        }
    }

    LaunchedEffect(isPlaying) {
        player.playWhenReady = isPlaying
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        }
    )
}
