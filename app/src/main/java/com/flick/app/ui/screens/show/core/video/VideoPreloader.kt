package com.flick.app.ui.screens.show.core.video

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import timber.log.Timber

/**
 * Preloads the next video's player surface while the current video plays.
 * This enables near-instant playback when swiping to the next video.
 *
 * Based on TikTok's video pre-rendering optimization technique.
 */
class VideoPreloader(
    private val playerCache: ExoPlayerCache
) {
    private var preloadedPlayer: ExoPlayer? = null
    private var preloadedVideoUrl: String? = null

    /**
     * Preloads the next video by preparing (but not playing) an ExoPlayer.
     * If the same URL is already preloaded, this is a no-op.
     *
     * @param videoUrl URL of the video to preload
     */
    fun preloadNext(videoUrl: String) {
        if (videoUrl.isBlank()) {
            Timber.d("VideoPreloader: Empty URL, skipping preload")
            return
        }

        if (videoUrl == preloadedVideoUrl) {
            Timber.d("VideoPreloader: Video already preloaded: $videoUrl")
            return
        }

        // Release any previously preloaded player
        releasePreloaded()

        Timber.d("VideoPreloader: Preloading video: $videoUrl")

        try {
            preloadedPlayer = playerCache.getPlayer().apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                repeatMode = Player.REPEAT_MODE_ONE
                prepare()
                playWhenReady = false // Don't play, just prepare
            }
            preloadedVideoUrl = videoUrl
            Timber.d("VideoPreloader: Successfully preloaded: $videoUrl")
        } catch (e: Exception) {
            Timber.e(e, "VideoPreloader: Failed to preload video")
            preloadedPlayer = null
            preloadedVideoUrl = null
        }
    }

    /**
     * Gets the preloaded player if the URL matches.
     * The preloaded player is cleared after retrieval (one-time use).
     *
     * @param videoUrl URL to check against preloaded video
     * @return The preloaded ExoPlayer if URL matches, null otherwise
     */
    fun getPreloadedPlayer(videoUrl: String): ExoPlayer? {
        return if (videoUrl == preloadedVideoUrl && preloadedPlayer != null) {
            Timber.d("VideoPreloader: Using preloaded player for: $videoUrl")
            val player = preloadedPlayer
            preloadedPlayer = null
            preloadedVideoUrl = null
            player
        } else {
            Timber.d("VideoPreloader: No preloaded player for: $videoUrl (preloaded: $preloadedVideoUrl)")
            null
        }
    }

    /**
     * Checks if a video is currently preloaded.
     *
     * @param videoUrl URL to check
     * @return true if the video is preloaded and ready
     */
    fun isPreloaded(videoUrl: String): Boolean {
        return videoUrl == preloadedVideoUrl && preloadedPlayer != null
    }

    /**
     * Releases the currently preloaded player back to the cache.
     */
    fun releasePreloaded() {
        preloadedPlayer?.let { player ->
            Timber.d("VideoPreloader: Releasing preloaded player")
            playerCache.releasePlayer(player)
        }
        preloadedPlayer = null
        preloadedVideoUrl = null
    }

    /**
     * Clears all preloaded resources. Call when the screen is destroyed.
     */
    fun clear() {
        releasePreloaded()
        Timber.d("VideoPreloader: Cleared")
    }
}
