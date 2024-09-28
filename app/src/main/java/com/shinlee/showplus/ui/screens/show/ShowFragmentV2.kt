package com.shinlee.showplus.ui.screens.show

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.showplus.databinding.ShowFragmentV2Binding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowFragmentV2 : Fragment() {

    private var _binding: ShowFragmentV2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: ShowViewModelV2 by viewModel()

    private var currentPlayingPosition: Int = RecyclerView.NO_POSITION

    private var playPauseAnimatorSet: AnimatorSet? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressAction = object : Runnable {
        override fun run() {
            updateProgress()
            handler.postDelayed(this, 1000) // Update every 100ms
        }
    }

    private val videoAdapter = VideoAdapterV2(emptyList(), object : VideoAdapterV2.OnClickListener {
        override fun onSingleClick() {
            togglePlayPause()
        }
    }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShowFragmentV2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupScrollListener()
        togglePlayPause()
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerView.adapter = videoAdapter
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    private fun observeViewModel() {
        viewModel.videos.observe(viewLifecycleOwner) {
            videoAdapter.updateVideos(it)
        }

    }

    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                playVideoInCenter()
                // loadThumbnailForCenterItem()
            }
        })
    }

    private fun playVideoInCenter() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        val centerPosition = findCenterPosition(layoutManager)
        if (centerPosition != RecyclerView.NO_POSITION && centerPosition != currentPlayingPosition) {
            playVideoAtPosition(centerPosition, viewModel.currentPlaybackPosition)
        }
    }

    private fun findCenterPosition(layoutManager: LinearLayoutManager): Int {
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        return (firstVisible..lastVisible).minByOrNull { position ->
            val view =
                layoutManager.findViewByPosition(position) ?: return@minByOrNull Int.MAX_VALUE
            val center = (view.top + view.bottom) / 2
            Math.abs(binding.recyclerView.height / 2 - center)
        } ?: RecyclerView.NO_POSITION
    }

    private fun playVideoAtPosition(position: Int, seekPosition: Long) {
        stopCurrentVideo()
        stopProgressUpdates()

        val newViewHolder =
            binding.recyclerView.findViewHolderForAdapterPosition(position) as? VideoAdapterV2.VideoViewHolder
                ?: return
        val player = viewModel.getPlayer()

        newViewHolder.getPlayerView().player = player
        viewModel.videos.value?.let { videos ->
            if (position in videos.indices) {
                player.apply {
                    setMediaItem(MediaItem.fromUri(videos[position].videoLink))
                    //seekTo(seekPosition)
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    prepare()
                    playWhenReady = true
                }

                currentPlayingPosition = position
                viewModel.currentPlayingPosition = position
                setupPlayerListeners(player)
            }
        }
    }

    private fun stopCurrentVideo() {
        if (currentPlayingPosition != RecyclerView.NO_POSITION) {
            val currentViewHolder =
                binding.recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition) as? VideoAdapterV2.VideoViewHolder
            currentViewHolder?.getPlayerView()?.player?.let { player ->
                (player as? ExoPlayer)?.let { exoPlayer ->
                    viewModel.releasePlayer(exoPlayer)
                }
                currentViewHolder.getPlayerView().player = null
            }
        }
    }

    private fun setupPlayerListeners(player: ExoPlayer) {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
                    Player.STATE_READY -> {
                        binding.progressBar.visibility = View.GONE
                        //hideThumbnail()
                        startProgressUpdates()
                    }

                    Player.STATE_ENDED, Player.STATE_IDLE -> {
                        stopProgressUpdates()
                        //showThumbnail()
                    }
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                viewModel.currentPlaybackPosition = newPosition.positionMs
            }
        })
    }

    private fun togglePlayPause() {
        val player =
            (binding.recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition) as? VideoAdapterV2.VideoViewHolder)?.getPlayerView()?.player as? ExoPlayer
        player?.let {
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

    private fun updateProgress() {
        val player =
            (binding.recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition) as? VideoAdapterV2.VideoViewHolder)?.getPlayerView()?.player as? ExoPlayer
        player?.let {
            val duration = it.duration
            val position = it.currentPosition
            if (duration > 0) {
                val progressValue = (position.toFloat() / duration.toFloat()) * 100f
                binding.videoProgressBar.progress = progressValue.toInt()
            }
            viewModel.currentPlaybackPosition = position
        }
    }

    private fun startProgressUpdates() {
        handler.removeCallbacks(updateProgressAction)
        handler.post(updateProgressAction)
    }

    private fun stopProgressUpdates() {
        handler.removeCallbacks(updateProgressAction)
    }

    private fun pauseCurrentVideo() {
        if (currentPlayingPosition != RecyclerView.NO_POSITION) {
            val currentViewHolder =
                binding.recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition) as? VideoAdapterV2.VideoViewHolder
            currentViewHolder?.getPlayerView()?.player?.let { player ->
                viewModel.currentPlaybackPosition = player.currentPosition
                player.playWhenReady = false
            }
        }
    }

    private fun resumeCurrentVideo() {
        if (currentPlayingPosition != RecyclerView.NO_POSITION) {
            val currentViewHolder =
                binding.recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition) as? VideoAdapterV2.VideoViewHolder
            currentViewHolder?.getPlayerView()?.player?.let { player ->
                startProgressUpdates()
                player.playWhenReady = true
            }
        }
    }


    override fun onResume() {
        super.onResume()
        resumeCurrentVideo()
    }

    override fun onPause() {
        super.onPause()
        pauseCurrentVideo()
        stopProgressUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseAllPlayers()
        handler.removeCallbacks(updateProgressAction)
        playPauseAnimatorSet?.cancel()
    }

}