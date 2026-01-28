package com.flick.app.ui.screens.show.composable

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
 * Composable wrapper for ExoPlayer PlayerView with thumbnail support.
 * Shows thumbnail while video is loading/buffering for better UX.
 *
 * @param videoUrl URL of the video to play
 * @param thumbnailUrl URL of the thumbnail to show while loading
 * @param player ExoPlayer instance to use for playback
 * @param isPlaying Whether the video should be playing
 * @param onPlayerReady Callback when player is ready
 * @param modifier Modifier for the composable
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    thumbnailUrl: String? = null,
    player: ExoPlayer?,
    isPlaying: Boolean,
    onPlayerReady: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isMediaSet by remember { mutableStateOf(false) }
    var isVideoReady by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(true) }

    // Set media item when player and URL are available
    LaunchedEffect(player, videoUrl) {
        // Reset states when video changes
        isVideoReady = false
        isBuffering = true
        isMediaSet = false

        player?.let { exoPlayer ->
            if (videoUrl.isNotBlank()) {
                try {
                    // Check if this player already has the media prepared (preloaded)
                    val currentUri = exoPlayer.currentMediaItem?.localConfiguration?.uri?.toString()
                    if (currentUri == videoUrl && exoPlayer.playbackState == Player.STATE_READY) {
                        // Already prepared (preloaded player)
                        Timber.d("Using preloaded media: $videoUrl")
                        isMediaSet = true
                        isVideoReady = true
                        isBuffering = false
                    } else {
                        // Need to prepare
                        val mediaItem = MediaItem.fromUri(videoUrl)
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                        isMediaSet = true
                        Timber.d("Media set: $videoUrl")
                    }
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
                if (isPlaying && isVideoReady) {
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
                        isVideoReady = true
                        isBuffering = false
                        onPlayerReady()
                    }
                    Player.STATE_BUFFERING -> {
                        Timber.d("Player STATE_BUFFERING")
                        isBuffering = true
                    }
                    Player.STATE_ENDED -> {
                        Timber.d("Player STATE_ENDED")
                    }
                    Player.STATE_IDLE -> {
                        Timber.d("Player STATE_IDLE")
                        isVideoReady = false
                    }
                }
            }

            override fun onRenderedFirstFrame() {
                Timber.d("First frame rendered")
                isVideoReady = true
                isBuffering = false
            }
        }

        player?.addListener(listener)

        onDispose {
            player?.removeListener(listener)
        }
    }

    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Video Player
        AndroidView(
            modifier = Modifier.fillMaxSize(),
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

        // Thumbnail overlay - show while video is not ready
        if (!isVideoReady && !thumbnailUrl.isNullOrBlank()) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Video thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Loading indicator - show while buffering
        if (isBuffering && isPlaying) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color.White,
                strokeWidth = 3.dp
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
