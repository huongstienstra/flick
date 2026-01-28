package com.flick.common.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.media3.exoplayer.ExoPlayer
import com.flick.common.databinding.ViewVideoControllerOverlayBinding

class VideoControllerOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ViewVideoControllerOverlayBinding =
        ViewVideoControllerOverlayBinding.inflate(LayoutInflater.from(context), this)

    private var isVisible = false
    private var hideHandler = Handler(Looper.getMainLooper())
    private var player: ExoPlayer? = null
    private var isSeeking = false

    init {
        setupViews()
        hide() // Initially hidden
    }


    private fun setupViews() {
        binding.seekBar.max = 1000 // For smoother seeking

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.let {
                        val position = (progress * it.duration / 1000f).toLong()
                        updateTimeText(position, it.duration)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
                removeCallbacks(hideRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                player?.let {
                    val position = (seekBar?.progress?.times(it.duration) ?: 0) / 1000
                    it.seekTo(position)
                }
                scheduleHide()
            }
        })

        // Update progress periodically
        post(object : Runnable {
            override fun run() {
                if (!isSeeking) {
                    updateProgress()
                }
                postDelayed(this, 16) // ~60fps updates
            }
        })
    }

    private val hideRunnable = Runnable { hide() }

    fun setPlayer(exoPlayer: ExoPlayer) {
        player = exoPlayer
        updateDuration()
    }

    fun show() {
        if (!isVisible) {
            isVisible = true
            binding.controlScrim.visibility = View.VISIBLE
            binding.controlsContainer.visibility = View.VISIBLE

            // Fade in animation
            binding.controlScrim.alpha = 0f
            binding.controlsContainer.alpha = 0f

            binding.controlScrim.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            binding.controlsContainer.animate()
                .alpha(1f)
                .setDuration(300)
                .start()

            scheduleHide()
        }
    }

    private fun hide() {
        if (isVisible) {
            isVisible = false

            // Fade out animation
            binding.controlScrim.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.controlScrim.visibility = View.GONE
                }
                .start()

            binding.controlsContainer.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.controlsContainer.visibility = View.GONE
                }
                .start()
        }
    }

    private fun scheduleHide() {
        removeCallbacks(hideRunnable)
        postDelayed(hideRunnable, 3000)
    }

    private fun updateProgress() {
        player?.let {
            if (!isSeeking && it.duration > 0) {
                val position = it.currentPosition
                val duration = it.duration
                val progress = ((position * 1000L) / duration).toInt()
                binding.seekBar.progress = progress
                updateTimeText(position, duration)

                // Update buffered progress
                val bufferedPosition = it.bufferedPosition
                val bufferedProgress = ((bufferedPosition * 1000L) / duration).toInt()
                binding.seekBar.secondaryProgress = bufferedProgress
            }
        }
    }

    private fun updateDuration() {
        player?.let {
            val duration = it.duration
            if (duration > 0) {
                binding.duration.text = String.format(
                    "-%s",
                    formatTime(duration - it.currentPosition)
                )
            }
        }
    }

    private fun updateTimeText(position: Long, duration: Long) {
        binding.currentTime.text = formatTime(position)
        binding.duration.text = String.format("-%s", formatTime(duration - position))
    }

    private fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val remainingSeconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    fun release() {
        removeCallbacks(hideRunnable)
        player = null
    }

}