package com.shinlee.showplus.ui.screens.show

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.shinlee.showplus.databinding.VideoItemViewV2Binding

class VideoAdapterV2(
    private var videos: List<VideoShow>,
    private val player: ExoPlayer,
    private val listener: OnClickListener? = null
) :
    RecyclerView.Adapter<VideoAdapterV2.VideoViewHolder>() {

    private var playPauseAnimatorSet: AnimatorSet? = null

    fun updateVideos(newVideos: List<VideoShow>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(private val binding: VideoItemViewV2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        private val updateProgressAction = object : Runnable {
            override fun run() {
                updateVideoProgress()
                binding.videoProgressBar.postDelayed(this, 1000) // Update every second
            }
        }

         fun updateLikeIcon(isFavourite: Boolean) {
            val iconRes = if (isFavourite) {
                com.shinlee.common.R.drawable.ic_redlike
            } else {
                com.shinlee.common.R.drawable.ic_like
            }
            binding.btnLike.setIcon(iconRes)
        }

        fun bind(video: VideoShow, position: Int) {
            updateLikeIcon(video.isFavourite)
            //set thumbnail
            Glide.with(itemView)
                .load(video.thumbnail)
                .apply(RequestOptions().centerCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.thumbnail)

            binding.playerView.setOnClickListener {
                togglePlayPause()
            }

            binding.btnLike.setOnClickListener {
                listener?.onLikeVideo(video, position)
            }

            binding.btnComment.setOnClickListener {
                listener?.onComment()
            }

            binding.btnMore.setOnClickListener {
                listener?.onSeeMore()
            }

            binding.btnSend.setOnClickListener {
                listener?.onShare(video.videoLink)
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
                            startProgressUpdates()
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
                        .setUri(videos[position].videoLink)
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
                    binding.playPauseIcon.setImageResource(com.shinlee.common.R.drawable.ic_video_pause)
                } else {
                    player.play()
                    binding.playPauseIcon.setImageResource(com.shinlee.common.R.drawable.ic_video_play)
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

        private fun updateVideoProgress() {
            val duration = player.duration
            val position = player.currentPosition
            if (duration > 0) {
                val progress = (position * 1000L / duration).toInt()
                binding.videoProgressBar.progress = progress
            }
        }

        private fun startProgressUpdates() {
            binding.videoProgressBar.removeCallbacks(updateProgressAction)
            binding.videoProgressBar.post(updateProgressAction)
        }

        fun stopProgressUpdates() {
            binding.videoProgressBar.removeCallbacks(updateProgressAction)
        }


        //fixme: more to Player or Viewmodel
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
        holder.bind(videos[position], position)
    }

    override fun getItemCount() = videos.size

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        //playPauseAnimatorSet?.removeAllListeners()
        holder.stopProgressUpdates()
    }


    interface OnClickListener {
        fun onShare(url: String)
        fun onLikeVideo(video: VideoShow, position: Int)
        fun onComment()
        fun onSubscribe()
        fun onSeeMore()
    }
}