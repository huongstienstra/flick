package com.shinlee.showplus.ui.screens.show.v1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.recyclerview.widget.PagerSnapHelper
import com.shinlee.showplus.databinding.ShowFragmentBinding
import com.shinlee.showplus.ui.screens.show.core.video.availableCodecsNum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@OptIn(UnstableApi::class)
class ShowFragment : Fragment() {

    private val viewModel: ShowViewModel by viewModel {
        parametersOf(minOf(1, availableCodecsNum())) // Pass your maxPoolSize here
    }

    private var _binding: ShowFragmentBinding? = null
    private val binding get() = _binding!!


    private val adapter: VideoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideoAdapter(
            viewModel.playersPool,
            lifecycleScope,
            viewModel.playersActions,
            Dispatchers.Main,
            viewModel::updatePlaybackPosition,
            object: VideoAdapter.OnClickListener {
                override fun onSingleClick() {
                    Log.d("video_list", "Video")
                }

                override fun onDoubleClick() {
                }

                override fun onShare() {
                    Log.d("video_list", "Share")
                }

                override fun onLikeVideo() {
                    Log.d("video_list", "Like")
                }

                override fun onComment() {
                    Log.d("video_list", "Comment")
                }

            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoList.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.videoList)

        viewModel.getVideos()

        lifecycleScope.launch {
//            viewModel.playbackPositions
//                .onEach { playbackPositions -> adapter.playbackPositions = playbackPositions }
//                .launchIn(this)

            viewModel.videoStateFlow
                .onEach(adapter::updateVideoUrls)
                .launchIn(this)

           // Log.e("video_list", "${availableCodecsNum()}")


            // Pre-cache videos when the activity is created
//            viewModel.videoUrls
//                .collectLatest { videoUrls ->
//                    if (videoUrls.isNotEmpty()) {
//                        preCacheVideos(videoUrls, cacheModule)
//                    }
//                }
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d("video_list", "onStart")
        if (Util.SDK_INT > 23)
            viewModel.restartPlayers()
    }

    override fun onResume() {
        super.onResume()
        Log.d("video_list", "onResume")
        if (Util.SDK_INT <= 23)
            viewModel.restartPlayers()
    }

    override fun onPause() {
        Log.d("video_list", "onPause")
        if (Util.SDK_INT <= 23)
            viewModel.releasePlayers()
        super.onPause()
    }

    override fun onStop() {
        Log.d("video_list", "onStop")
        if (Util.SDK_INT > 23)
            viewModel.releasePlayers()
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("video_list", "onDestroyView called")
        viewModel.releasePlayers()
        _binding = null
        super.onDestroyView()
    }


}