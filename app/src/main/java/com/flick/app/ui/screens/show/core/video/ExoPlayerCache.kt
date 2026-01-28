package com.flick.app.ui.screens.show.core.video

import android.app.Application
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerCache(private val application: Application) {
    private val players = mutableListOf<ExoPlayer>()

    fun getPlayer(): ExoPlayer {
        return if (players.isNotEmpty()) {
            players.removeAt(0)
        } else {
            ExoPlayer.Builder(application).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                            // If the error is due to being behind the live window, we can try to recover
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