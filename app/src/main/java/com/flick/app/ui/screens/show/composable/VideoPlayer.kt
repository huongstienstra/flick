package com.flick.app.ui.screens.show.composable

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
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
    player: ExoPlayer?,
    isPlaying: Boolean,
    onPlayerReady: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isMediaSet by remember { mutableStateOf(false) }

    // Set media item when player and URL are available
    LaunchedEffect(player, videoUrl) {
        player?.let { exoPlayer ->
            if (videoUrl.isNotBlank()) {
                try {
                    val mediaItem = MediaItem.fromUri(videoUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
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
                exoPlayer.playWhenReady = isPlaying
                if (isPlaying) {
                    onPlayerReady()
                }
            }
        }
    }

    // Player listener for state changes
    DisposableEffect(player) {
        val listener = object : Player.Listener {
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

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        update = { playerView ->
            playerView.player = player
        }
    )
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
