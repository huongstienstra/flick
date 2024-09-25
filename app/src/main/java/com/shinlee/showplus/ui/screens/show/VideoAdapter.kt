package com.shinlee.showplus.ui.screens.show


import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.shinlee.showplus.R
import com.shinlee.showplus.databinding.VideoItemViewBinding
import com.shinlee.showplus.ui.screens.show.core.video.PlayersAction
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class VideoAdapter(
    private val playersPool: PlayersPool,
    private val lifecycleScope: CoroutineScope,
    private val playersActions: Flow<PlayersAction>,
    private val dispatcher: CoroutineDispatcher,
    private val updatePlaybackPosition: (Int, Long) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    private var videoUrls: List<String> = listOf()
    var playbackPositions: List<Long> = listOf()

    private var isFirstFrameRendered = false

    fun updateVideoUrls(videoUrls: List<String>) {
        this.videoUrls = videoUrls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(
            VideoItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoUrls[position], playbackPositions[position])
    }

    override fun getItemCount(): Int = videoUrls.size

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        holder.detach()
        super.onViewDetachedFromWindow(holder)
    }

    inner class VideoViewHolder(
        private val binding: VideoItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var videoScope: CoroutineScope
        private lateinit var playerChannel: Channel<Player>
        private var playJob: Job? = null
        private var restartJob: Job? = null

        fun bind(url: String, playbackPosition: Long) {
            videoScope = CoroutineScope(Job() + dispatcher)
            bindPlayer(url, playbackPosition)
            if (restartJob === null) {
                restartJob = playersActions
                    .onEach { action ->
                        when (action) {
                            PlayersAction.RELEASE -> with(binding.playerView) {
//                                Timber.tag(VIDEO_LIST).d(
//                                    "Release player: url = %s",
//                                    url.substringAfterLast('/')
//                                )
                                updatePlaybackPosition(
                                    absoluteAdapterPosition,
                                    player?.currentPosition ?: 0
                                )
                                player?.run {
                                    release()
                                    playersPool.release(this)
                                }
                                player = null
                            }

                            PlayersAction.RESTART -> {
                                if (absoluteAdapterPosition in videoUrls.indices) {
                                    bindPlayer(
                                        videoUrls[absoluteAdapterPosition],
                                        playbackPositions[absoluteAdapterPosition]
                                    )
                                }
                            }
                        }
                    }.launchIn(lifecycleScope)
            }
        }


        fun attach() {
            if (!videoScope.isActive) {
                videoScope = CoroutineScope(Job() + dispatcher)
                bindPlayer(
                    videoUrls[absoluteAdapterPosition],
                    playbackPositions[absoluteAdapterPosition]
                )
            }
        }

        private fun bindPlayer(url: String, playbackPosition: Long) {
            binding.playerView.apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                setShowNextButton(false)
                setShowPreviousButton(false)
                setShowVrButton(false)
                setShowRewindButton(false)
                setShowFastForwardButton(false)
                useController = false
            }
            playJob?.cancel()
            playJob = videoScope.launch {
//                Timber.tag(VIDEO_LIST).d(
//                    "Awaiting for player url = %s, playbackPosition = %d",
//                    url.substringAfterLast('/'),
//                    playbackPosition
//                )
                playersPool.acquire()
                    .also { playerChannel = it }
                    .receive()
                    .run {
//                        Timber.tag(VIDEO_LIST).d(
//                            "Playing url = %s, playbackPosition = %d",
//                            url.substringAfterLast('/'),
//                            playbackPosition
//                        )
                        binding.playerView.player = this
                        //setMediaItem(MediaItem.fromUri(url))
                        setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                        playWhenReady = true
                        this.addListener(
                            object : Player.Listener {
                                override fun onRenderedFirstFrame() {
                                    super.onRenderedFirstFrame()
                                    isFirstFrameRendered = true
                                    binding.thumbnail.visibility = View.GONE
                                }

                                override fun onPlaybackStateChanged(playbackState: Int) {
                                    super.onPlaybackStateChanged(playbackState)
                                    if (playbackState == Player.STATE_READY && !isFirstFrameRendered) {
                                        // If the video is ready but the first frame hasn't rendered, keep the thumbnail visible
                                        binding.thumbnail.visibility = View.VISIBLE
                                    }
                                }
                            }
                        )

                        seekTo(0, playbackPosition)
                        prepare()
                    }
            }
        }

        fun detach() {
            with(binding.playerView) {
                playersPool.removeFromAwaitingQueue(playerChannel)
                player?.run {
                    playWhenReady = false
                    stop()
                    updatePlaybackPosition(absoluteAdapterPosition, currentPosition)
                    playersPool.stop(this)
                }
//                Timber.tag(VIDEO_LIST).d(
//                    "Player detached: url = %s, playback position = %d",
//                    videoUrls[absoluteAdapterPosition].substringAfterLast('/'),
//                    player?.currentPosition
//                )
                videoScope.cancel()
                player = null
            }
        }
    }
}