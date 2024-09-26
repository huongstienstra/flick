package com.shinlee.showplus.ui.screens.show


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.shinlee.showplus.databinding.VideoItemViewBinding
import com.shinlee.showplus.ui.screens.show.core.video.PlayersAction
import com.shinlee.showplus.ui.screens.show.core.video.PlayersPool
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val updatePlaybackPosition: (Int, Long) -> Unit,
    private val listener: OnClickListener? = null
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    private var videoUrls: List<String> = listOf()
    var playbackPositions: List<Long> = listOf()

    private var playPauseAnimatorSet: AnimatorSet? = null
    private var isFirstFrameRendered = false

    private var isFavourite = false

    // To handle seekbar updates
    private var isSeeking: Boolean = false

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
        playPauseAnimatorSet?.cancel()
        super.onViewDetachedFromWindow(holder)
    }

    inner class VideoViewHolder(
        private val binding: VideoItemViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var videoScope: CoroutineScope
        private lateinit var playerChannel: Channel<Player>
        private var playJob: Job? = null
        private var restartJob: Job? = null
        private var progressJob: Job? = null  // Declare progressJob here

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

            with(binding) {
                btnSend.setOnClickListener {
                    listener?.onShare()
                }
                btnLike.setOnClickListener {
                    isFavourite = !isFavourite
                    if (isFavourite) {
                        binding.btnLike.setIcon(com.shinlee.common.R.drawable.ic_redlike)
                    } else binding.btnLike.setIcon(com.shinlee.common.R.drawable.ic_like)
                    listener?.onLikeVideo()
                }
                btnComment.setOnClickListener {
                    listener?.onComment()
                }

                playerView.setOnClickListener {
                    listener?.onSingleClick()
                    togglePlayPause()
                }

            }
        }

        private fun togglePlayPause() {
            val player = binding.playerView.player ?: return

            if (player.isPlaying) {
                player.pause()
                binding.playPauseIcon.setImageResource(com.shinlee.common.R.drawable.ic_video_pause)
            } else {
                player.play()
                binding.playPauseIcon.setImageResource(com.shinlee.common.R.drawable.ic_video_play)
            }

           animatePlayPauseIcon()
        }

        private fun animatePlayPauseIcon() {
            playPauseAnimatorSet?.cancel()

            val fadeIn = ObjectAnimator.ofFloat(binding.playPauseIcon, "alpha", 0f, 1f).apply {
                duration = 300 // Fade-in duration (300 ms)
                startDelay = 0 // No delay for fade-in
                binding.playPauseIcon.visibility = View.VISIBLE // Ensure icon is visible
            }

            val fadeOut = ObjectAnimator.ofFloat(binding.playPauseIcon, "alpha", 1f, 0f).apply {
                duration = 500 // Fade-out duration (500 ms)
                startDelay = 1000 // Delay fade-out for 2 seconds (show icon for 2 seconds)
            }

            // Use AnimatorSet to play fade-in followed by fade-out sequentially
            playPauseAnimatorSet = AnimatorSet().apply {
                playSequentially(fadeIn, fadeOut)
                start()
            }
        }


        fun attach() {
            if (!videoScope.isActive) {
                videoScope = CoroutineScope(Job() + dispatcher)
                bindPlayer(
                    videoUrls[absoluteAdapterPosition],
                    playbackPositions[absoluteAdapterPosition]
                )
//                progressJob?.cancel()
//                startProgressBarUpdater()
            }
        }

        private fun bindPlayer(url: String, playbackPosition: Long) {
            binding.playerView.apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
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
                        setMediaItem(MediaItem.fromUri(url))
                        //setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                        playWhenReady = true
                        repeatMode = ExoPlayer.REPEAT_MODE_ONE
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

//        private fun startProgressBarUpdater() {
//            progressJob = videoScope.launch {
//                while (isActive) {
//                    val player = binding.playerView.player ?: continue
//                    val currentPosition = player.currentPosition
//                    val duration = player.duration
//
//                    // Update the progress bar
//                    if (duration > 0 && !isSeeking) {
//                        val progress = (currentPosition * 100 / duration).toInt()
//                        binding.videoProgressBar.progress = progress
//                    }
//
//                    delay(1000) // Update every second
//                }
//            }
//        }


        fun detach() {
            binding.playPauseIcon.visibility = View.GONE
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
                progressJob?.cancel()
                player = null
            }
        }
    }

    interface OnClickListener {
        fun onSingleClick()
        fun onDoubleClick()
        fun onShare()
        fun onLikeVideo()
        fun onComment()
    }
}
