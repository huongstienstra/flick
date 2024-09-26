package com.shinlee.showplus.ui.screens.show.core.video

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.channels.Channel
import java.util.LinkedList
import java.util.Queue

class PlayersPool(
    private val context: Context,
    private val maxPoolSize: Int,
) {
    private val unlockedPlayers: MutableList<Player> =
        mutableListOf(ExoPlayer.Builder(context).build())
    private val lockedPlayers: MutableList<Player> = mutableListOf()

    private val waitingQueue: Queue<Channel<Player>> = LinkedList()

    @OptIn(UnstableApi::class)
    @Synchronized
    fun acquire(): Channel<Player> =
        if (unlockedPlayers.isEmpty()) {
            if (lockedPlayers.size >= maxPoolSize) {
                Channel<Player>(capacity = 1).also { channel -> waitingQueue.offer(channel) }
            } else {
                Channel<Player>(capacity = 1).apply {

                    val trackSelector = DefaultTrackSelector(context).apply {
                        setParameters(buildUponParameters().setMaxVideoSize(1080, 1920))
                    }
                    // TODO such settings might be an issue under certain conditions, needs testing
                    val loadControl = DefaultLoadControl.Builder().apply {
                        setBufferDurationsMs(
                            0, // DEFAULT_MIN_BUFFER_MS = 50_000;
                            0, // DEFAULT_MAX_BUFFER_MS = 50_000;
                            0, // DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500
                            0 // DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000;
                        )
                    }.build()
                    val exoPlayer = ExoPlayer.Builder(context).apply {
                        setTrackSelector(trackSelector)
                        setLoadControl(loadControl)
                        // TODO: add CacheModule
//                        setMediaSourceFactory(
//                            DefaultMediaSourceFactory(context)
//                                .setDataSourceFactory(cacheModule.cacheDataSourceFactory)
//                        )
                    }.build()
                    trySend(
                        exoPlayer.also(lockedPlayers::add)
                    )
                }
            }
        } else {
            Channel<Player>(capacity = 1).apply {
                trySend(unlockedPlayers.removeLast().also(lockedPlayers::add))
            }
        }.also {
            Log.d("video_list", "pool size =  ${lockedPlayers.size + unlockedPlayers.size}")
        }

    @Synchronized
    fun removeFromAwaitingQueue(channel: Channel<Player>) {
        waitingQueue.remove(channel)
    }

    @Synchronized
    fun release(player: Player) {
        lockedPlayers.remove(player)
    }

    @Synchronized
    fun stop(player: Player) {
        if (!reusePlayer(player)) {
            lockedPlayers.remove(player)
            unlockedPlayers.add(player)
        }
    }

    private fun reusePlayer(player: Player): Boolean =
        waitingQueue.poll()?.run {
            trySend(player)
            true
        } ?: false

    @Synchronized
    fun releaseAll() {
        waitingQueue.clear()
        unlockedPlayers.addAll(lockedPlayers)
        lockedPlayers.clear()
    }
}