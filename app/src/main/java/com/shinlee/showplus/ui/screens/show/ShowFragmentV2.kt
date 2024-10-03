package com.shinlee.showplus.ui.screens.show

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.currentRecomposeScope
import androidx.fragment.app.Fragment
import androidx.media3.common.util.Log
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
    private var player: ExoPlayer? = null

    private var currentPlayingPosition: Int = RecyclerView.NO_POSITION

    private var videoAdapter: VideoAdapterV2? = null

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

        //setup adapter
        player = viewModel.getPlayer()
        player?.let { it ->
            videoAdapter = VideoAdapterV2(emptyList(), it, object : VideoAdapterV2.OnClickListener {
                override fun onShare(url: String) {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, url)
                        type = "type/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }

                override fun onLikeVideo() {
                }

                override fun onComment() {
                }

                override fun onSubscribe() {
                }

                override fun onSeeMore() {
                }

            })
        }

        setupRecyclerView()
        observeViewModel()
        setupScrollListener()
        setListener()
    }

    private fun setListener() {
        binding.tabs.setOnTabSelectedListener { index ->
            when (index) {
                0 -> {
                    // Handle "Following" tab selection
                    // For example, load Following content
                }
                1 -> {
                    // Handle "For You" tab selection
                    // For example, load For You content
                }
            }

        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerView.adapter = videoAdapter
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    private fun observeViewModel() {
        viewModel.videos.observe(viewLifecycleOwner) {
            videoAdapter?.updateVideos(it)
            Log.e("video", "update list ${it.size}")
        }

        if (currentPlayingPosition == RecyclerView.NO_POSITION) {
            binding.recyclerView.post {
                playFirstVisibleVideo()
            }
        }
    }

    private fun playFirstVisibleVideo() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstVisiblePosition != RecyclerView.NO_POSITION) {
            playVideoAtPosition(firstVisiblePosition)
        }
    }

    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e("video_list", "$newState")
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val centerPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

                    if (centerPosition != RecyclerView.NO_POSITION && centerPosition != currentPlayingPosition) {
                        playVideoAtPosition(centerPosition)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                for (i in firstVisible..lastVisible) {
                    if (i != currentPlayingPosition) {
                        val viewHolder =
                            recyclerView.findViewHolderForAdapterPosition(i) as? VideoAdapterV2.VideoViewHolder
                                ?: continue
                        viewHolder.showThumbnail()
                    }
                }
            }
        })
    }

    private fun playVideoAtPosition(position: Int) {
        val newViewHolder =
            binding.recyclerView.findViewHolderForAdapterPosition(position) as? VideoAdapterV2.VideoViewHolder
                ?: return

        newViewHolder.playVideoAtPosition(position)
        currentPlayingPosition = position
    }


    private fun pauseCurrentVideo() {
        player?.playWhenReady = false
    }

    private fun resumeCurrentVideo() {
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        pauseCurrentVideo()
    }

    override fun onResume() {
        super.onResume()
        resumeCurrentVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseAllPlayers()
    }

}