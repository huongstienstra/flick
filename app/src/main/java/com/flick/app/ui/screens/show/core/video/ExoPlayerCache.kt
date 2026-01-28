package com.flick.app.ui.screens.show.core.video

import android.app.Application
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import timber.log.Timber

/**
 * Manages a pool of ExoPlayer instances with adaptive buffering based on network conditions.
 *
 * Features:
 * - Player pooling to reduce allocation overhead
 * - Dynamic buffer sizing based on network quality
 * - Automatic error recovery for live window issues
 */
class ExoPlayerCache(
    private val application: Application,
    private val networkMonitor: NetworkMonitor
) {
    private val players = mutableListOf<ExoPlayer>()
    private var currentNetworkQuality = NetworkMonitor.NetworkQuality.GOOD

    init {
        // Get initial network quality
        updateNetworkQuality()
    }

    /**
     * Updates the network quality assessment.
     * Call this periodically or when network changes are detected.
     */
    fun updateNetworkQuality() {
        currentNetworkQuality = networkMonitor.getNetworkQuality()
        Timber.d("ExoPlayerCache: Updated network quality to $currentNetworkQuality")
    }

    /**
     * Creates a LoadControl with buffer settings adapted to current network conditions.
     * Faster networks get smaller initial buffers for quicker playback start.
     * Slower networks get larger buffers to prevent rebuffering.
     */
    private fun createAdaptiveLoadControl(): LoadControl {
        val quality = currentNetworkQuality
        val minBuffer = NetworkMonitor.Companion.BufferConfig.getMinBufferMs(quality)
        val maxBuffer = NetworkMonitor.Companion.BufferConfig.getMaxBufferMs(quality)

        Timber.d("ExoPlayerCache: Creating LoadControl for $quality - min: ${minBuffer}ms, max: ${maxBuffer}ms")

        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                minBuffer,
                maxBuffer,
                NetworkMonitor.Companion.BufferConfig.BUFFER_FOR_PLAYBACK_MS,
                NetworkMonitor.Companion.BufferConfig.BUFFER_FOR_REBUFFER_MS
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    }

    fun getPlayer(): ExoPlayer {
        // Update network quality when getting a new player
        updateNetworkQuality()

        return if (players.isNotEmpty()) {
            players.removeAt(0)
        } else {
            ExoPlayer.Builder(application)
                .setLoadControl(createAdaptiveLoadControl())
                .build()
                .apply {
                    addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                                // If the error is due to being behind the live window, we can try to recover
                                Timber.w("ExoPlayerCache: Recovering from BEHIND_LIVE_WINDOW error")
                                this@apply.seekToDefaultPosition()
                                this@apply.prepare()
                            }
                        }
                    })
                }
        }
    }

    fun releasePlayer(player: ExoPlayer) {
        player.stop()
        player.clearMediaItems()
        players.add(player)
    }

    fun releaseAllPlayers() {
        players.forEach { it.release() }
        players.clear()
    }
}