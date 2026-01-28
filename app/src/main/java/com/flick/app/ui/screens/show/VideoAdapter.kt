package com.flick.app.ui.screens.show

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flick.app.databinding.VideoItemViewV2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoAdapter(
    private val player: ExoPlayer,
    private val listener: OnClickListener? = null
) :
    PagingDataAdapter<VideoShow, VideoAdapter.VideoViewHolder>(VIDEO_COMPARATOR) {

    private var isSeeking = false
    private var progressUpdateJob: Job? = null
    private val progressUpdateScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


    companion object {
        private val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<VideoShow>() {
            override fun areItemsTheSame(oldItem: VideoShow, newItem: VideoShow): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoShow, newItem: VideoShow): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var playPauseAnimatorSet: AnimatorSet? = null

    inner class VideoViewHolder(private val binding: VideoItemViewV2Binding) :
        RecyclerView.ViewHolder(binding.root) {

//        private val updateProgressAction = object : Runnable {
//            override fun run() {
//                updateVideoProgress()
//                binding.videoProgressBar.postDelayed(this, 1000) // Update every second
//            }
//        }

        fun updateLikeIcon(isFavourite: Boolean) {
            val iconRes = if (isFavourite) {
                com.flick.common.R.drawable.ic_redlike
            } else {
                com.flick.common.R.drawable.ic_like
            }
            binding.btnLike.setIcon(iconRes)
        }

        fun bind(video: VideoShow, position: Int) {
            binding.tvName.text = video.profileNickname ?: ""
            binding.tvDesc.text = video.description ?: ""
            binding.tvTag.text = video.tags?.joinToString(", ") { it.content ?: "" } ?: ""
            binding.btnLike.setText(video.voteCount.toString())
            binding.btnComment.setText(video.commentCount.toString())


            binding.videoControllerOverlay.setPlayer(player)

            // Show controller on video click
            binding.playerView.setOnClickListener {
                binding.videoControllerOverlay.show()
            }

            if (!video.profilePhoto.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load(video.profilePhoto)
                    .placeholder(com.flick.common.R.drawable.avatar)
                    .apply(
                        RequestOptions()
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(binding.avatar)
            }

            if (!video.contestTitle.isNullOrEmpty()) {
                binding.contestView.visibility = View.VISIBLE
                binding.tvContestTitle.text = video.contestTitle
            } else binding.contestView.visibility = View.GONE

            updateLikeIcon(video.isFavourite)

            //set thumbnail
            Glide.with(itemView)
                .load(video.thumbnailUrl)
                .apply(RequestOptions().centerCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.thumbnail)

            // show thumbnail and play video for the first view holder
            showThumbnail()
            playVideoAtPosition(position)

//            binding.playerView.setOnClickListener {
//                togglePlayPause()
//            }

            binding.btnLike.setOnClickListener {
                listener?.onLikeVideo(video, position)
            }

            binding.btnComment.setOnClickListener {
                listener?.onComment(video)
            }

            binding.btnMore.setOnClickListener {
                listener?.onSeeMore()
            }

            binding.btnSend.setOnClickListener {
                video.videoUrl?.let { it1 -> listener?.onShare(it1) }
            }

            binding.btnSubscribe.setOnClickListener {
                listener?.onSubscribe()
            }

            //player listener
            player.addListener(object : Player.Listener {
                override fun onRenderedFirstFrame() {
                    super.onRenderedFirstFrame()
                    Log.e("video", "onRenderedFirstFrame ")
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            //bufferingProgressBar.visibility = View.VISIBLE
                            Log.e("video", "STATE_BUFFERING ")
                        }

                        Player.STATE_READY -> {
                            // bufferingProgressBar.visibility = View.GONE
                            //startProgressUpdates()
                            Log.e("video", "STATE_READY ")

                            binding.thumbnail.visibility = View.GONE
                            // Log.e("video", "STATE_READY $newViewHolder")

                        }

                        Player.STATE_ENDED, Player.STATE_IDLE -> {
                            stopProgressUpdates()
                            Log.e("video", "STATE_ENDED ")
                        }

                        else -> {
                            //Log.e("video", "$playbackState")
                        }
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                }
            })

           // setupSeekBar()

        }

//        private fun setupSeekBar() {
//            binding.videoProgressBar.apply {
//                max = 100
//
//                // Hide thumb initially
//                //thumb.mutate().alpha = 0
//
//                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                        if (fromUser && isSeeking) {
//                            val duration = player.duration
//                            val position = (progress * duration / 100f).toLong()
//                            // Optional: Update time indicator
//                            // updateTimeText(position, duration)
//                        }
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                        isSeeking = true
//                        stopProgressUpdates()
//                        // Show thumb when seeking starts
//                       // seekBar?.thumb?.mutate()?.alpha = 255
//                    }
//
//                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                        isSeeking = false
//                        seekBar?.let {
//                            val duration = player.duration
//                            val position = (it.progress * duration / 100f).toLong()
//                            player.seekTo(position)
//                        }
//                        // Hide thumb when seeking ends
//                       // seekBar?.thumb?.mutate()?.alpha = 0
//                        startProgressUpdates()
//                    }
//                })
//            }
//        }
//
//        private fun startProgressUpdates() {
//            stopProgressUpdates()
//            progressUpdateJob = progressUpdateScope.launch {
//                while (isActive) {
//                    withContext(Dispatchers.Main) {
//                        if (!isSeeking && player.duration > 0) {
//                            val progress = (player.currentPosition * 100f / player.duration).toInt()
//                            binding.videoProgressBar.progress = progress
//                        }
//                    }
//                    delay(16) // ~60 FPS update rate
//                }
//            }
//        }

        private fun stopProgressUpdates() {
            progressUpdateJob?.cancel()
            progressUpdateJob = null
        }

        fun showThumbnail() {
            binding.thumbnail.visibility = View.VISIBLE
        }


        @OptIn(UnstableApi::class)
        fun playVideoAtPosition(position: Int) {
            with(binding) {
                playerView.player = null
                playerView.player = player
                playerView.apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    useController = false
                }

                val currentMediaItem =
                    MediaItem.Builder()
                        .setUri(getItem(position)?.videoUrl)
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .build()

                val mediaSource = buildMediaSource(currentMediaItem)
                player?.apply {
                    setMediaSource(mediaSource)
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    prepare()
                    playWhenReady = true
                    Log.e("video", "playWhenReady $player")
                }
            }

        }

        private fun togglePlayPause() {
            player.let {
                if (player.isPlaying) {
                    player.pause()
                    binding.playPauseIcon.setImageResource(com.flick.common.R.drawable.ic_video_pause)
                } else {
                    player.play()
                    binding.playPauseIcon.setImageResource(com.flick.common.R.drawable.ic_video_play)
                }
                animatePlayPauseIcon()
            }
        }

        private fun animatePlayPauseIcon() {
            playPauseAnimatorSet?.cancel()

            val fadeIn = ObjectAnimator.ofFloat(binding.playPauseIcon, "alpha", 0f, 1f).apply {
                duration = 300
                startDelay = 0
                binding.playPauseIcon.visibility = View.VISIBLE
            }

            val fadeOut = ObjectAnimator.ofFloat(binding.playPauseIcon, "alpha", 1f, 0f).apply {
                duration = 500
                startDelay = 1000
            }

            playPauseAnimatorSet = AnimatorSet().apply {
                playSequentially(fadeIn, fadeOut)
                start()
            }
        }

//        private fun updateVideoProgress() {
//            val duration = player.duration
//            val position = player.currentPosition
//            if (duration > 0) {
//                val progress = (position * 1000L / duration).toInt()
//                binding.videoProgressBar.progress = progress
//            }
//        }

//        private fun startProgressUpdates() {
//            binding.videoProgressBar.removeCallbacks(updateProgressAction)
//            binding.videoProgressBar.post(updateProgressAction)
//        }
//
//        fun stopProgressUpdates() {
//            binding.videoProgressBar.removeCallbacks(updateProgressAction)
//        }


        //fixme: move to Player or Viewmodel
        @OptIn(UnstableApi::class)
        private fun buildMediaSource(mediaItem: MediaItem): MediaSource {
            val dataSourceFactory = DefaultHttpDataSource.Factory()
            val hlsMediaSource =
                HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
            return hlsMediaSource

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            VideoItemViewV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        if (video != null) {
            holder.bind(video, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        playPauseAnimatorSet?.removeAllListeners()
       // holder.stopProgressUpdates()
    }


    interface OnClickListener {
        fun onShare(url: String)
        fun onLikeVideo(video: VideoShow, position: Int)
        fun onComment(video: VideoShow)
        fun onSubscribe()
        fun onSeeMore()
    }
}