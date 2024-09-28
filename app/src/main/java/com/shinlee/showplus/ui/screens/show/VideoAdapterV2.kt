package com.shinlee.showplus.ui.screens.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.showplus.databinding.VideoItemViewV2Binding


class VideoAdapterV2(private var videos: List<VideoShow>,   private val listener: VideoAdapterV2.OnClickListener? = null) :
    RecyclerView.Adapter<VideoAdapterV2.VideoViewHolder>() {

    private var isFavourite = false

    fun updateVideos(newVideos: List<VideoShow>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(private val binding: VideoItemViewV2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        @androidx.annotation.OptIn(UnstableApi::class)
        fun bind(video: VideoShow) {
            binding.playerView.apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                useController = false
            }

            binding.playerView.setOnClickListener {
                listener?.onSingleClick()
            }

            binding.btnLike.setOnClickListener {
                isFavourite = !isFavourite
                if (isFavourite) {
                    binding.btnLike.setIcon(com.shinlee.common.R.drawable.ic_redlike)
                } else binding.btnLike.setIcon(com.shinlee.common.R.drawable.ic_like)
                //listener?.onLikeVideo()
            }
        }

        fun getPlayerView() = binding.playerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            VideoItemViewV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount() = videos.size


    interface OnClickListener {
        fun onSingleClick()
//        fun onDoubleClick()
//        fun onShare()
//        fun onLikeVideo()
//        fun onComment()
    }
}