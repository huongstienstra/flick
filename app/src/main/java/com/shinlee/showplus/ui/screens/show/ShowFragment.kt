package com.shinlee.showplus.ui.screens.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.Util
import androidx.recyclerview.widget.PagerSnapHelper
import com.shinlee.showplus.R
import com.shinlee.showplus.databinding.ShowFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShowFragment : Fragment() {
    private val viewModel: ShowViewModel by viewModel { parametersOf(2) }

    private var _binding: ShowFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: VideoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideoAdapter(
            viewModel.playersPool,
            lifecycleScope,
            viewModel.playersActions,
            Dispatchers.Main,
            viewModel::updatePlaybackPosition
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
        lifecycleScope.launch {
            viewModel.playbackPositions
                .onEach { playbackPositions -> adapter.playbackPositions = playbackPositions }
                .launchIn(this)

            viewModel.videoUrls
                .onEach(adapter::updateVideoUrls)
                .launchIn(this)

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