package com.flick.app.ui.screens.show.core.video

import android.content.Context
import androidx.annotation.OptIn
import timber.log.Timber
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
                    val loadControl = DefaultLoadControl.Builder().apply {
                        setBufferDurationsMs(
                            DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
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
            Timber.d("pool size = ${lockedPlayers.size + unlockedPlayers.size}")
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