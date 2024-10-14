package com.shinlee.showplus.ui.screens.show

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.shinlee.repository.VideoShow
import com.shinlee.showplus.databinding.ShowFragmentV2Binding
import com.shinlee.showplus.extension.awaitPost
import com.shinlee.showplus.extension.requestLoginDialog
import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.authentication.AuthenticationActivity
import com.shinlee.showplus.ui.screens.show.dialogs.CommentsBottomSheet
import com.shinlee.showplus.ui.screens.show.dialogs.DescriptionBottomSheet
import com.shinlee.showplus.ui.screens.show.dialogs.OnClickListener
import com.shinlee.showplus.ui.screens.show.dialogs.ReportBottomSheet
import com.shinlee.showplus.ui.screens.show.dialogs.VideoSeeMoreBottomSheet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowFragment : Fragment() {

    private var _binding: ShowFragmentV2Binding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel<MainViewModel>()
    private val viewModel: ShowViewModel by viewModel()

    private var player: ExoPlayer? = null

    private var currentPlayingPosition: Int = RecyclerView.NO_POSITION

    private lateinit var videoAdapter: VideoAdapter

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

        initializeVideoAdapter()
        setUpRecyclerView()
        observeViewModel()
        setupScrollListener()
        setTabListener()

    }

    private fun initializeVideoAdapter() {

        videoAdapter = VideoAdapter(viewModel.getPlayer(), object : VideoAdapter.OnClickListener {
            override fun onShare(url: String) {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "type/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            override fun onLikeVideo(video: VideoShow, position: Int) {
                if (mainViewModel.isLoggedIn()) {
                    video.isFavourite = !video.isFavourite
                    val viewHolder =
                        binding.recyclerView.findViewHolderForAdapterPosition(position) as? VideoAdapter.VideoViewHolder
                    viewHolder?.updateLikeIcon(isFavourite = video.isFavourite)

                    // Call API to update like status on the server
                } else {
                    this@ShowFragment.requestLoginDialog(
                        onNext = {
                            startActivity(Intent(context, AuthenticationActivity::class.java))
                        }, onCancel = {

                        }
                    )

                }

            }

            override fun onComment() {
                CommentsBottomSheet().showByTag(childFragmentManager)
            }

            override fun onSubscribe() {
            }

            override fun onSeeMore() {
                val bottomSheet = VideoSeeMoreBottomSheet()
                bottomSheet.showByTag(childFragmentManager, object : OnClickListener {
                    override fun onSeeDescription() {
                        DescriptionBottomSheet().showByTag(childFragmentManager)
                    }

                    override fun onReport() {
                        ReportBottomSheet().showByTag(childFragmentManager)
                    }

                })
            }

        })

        videoAdapter?.addLoadStateListener { loadState ->
            // Check if the initial load is complete and successful
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && (videoAdapter?.itemCount
                    ?: 0) > 0
            ) {

            }
        }


    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = videoAdapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
    }

    private fun setTabListener() {
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

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.videos.collectLatest { pagingData ->
                videoAdapter.submitData(pagingData)
            }
        }
    }


    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
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
                            recyclerView.findViewHolderForAdapterPosition(i) as? VideoAdapter.VideoViewHolder
                                ?: continue
                        viewHolder.showThumbnail()
                    }
                }
            }
        })
    }

    private fun playVideoAtPosition(position: Int) {
        val newViewHolder =
            binding.recyclerView.findViewHolderForAdapterPosition(position) as? VideoAdapter.VideoViewHolder
        newViewHolder?.let {
            newViewHolder.playVideoAtPosition(position)
            currentPlayingPosition = position
        }

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