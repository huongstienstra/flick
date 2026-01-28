package com.flick.app.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flick.network.ApiResult
import com.flick.repository.VideoRepository
import com.flick.app.ui.screens.show.VideoShow
import com.flick.app.ui.screens.show.mapping.toVideoShow

class SearchVideoPagingSource(
    private val repository: VideoRepository,
    private val query: String
) : PagingSource<Int, VideoShow>() {

    override fun getRefreshKey(state: PagingState<Int, VideoShow>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoShow> {
        val page = params.key ?: 1

        return try {
            val result = repository.searchVideos(query, page, params.loadSize)

            when (result) {
                is ApiResult.Success -> {
                    val videos = result.data.map { it.toVideoShow() }
                    LoadResult.Page(
                        data = videos,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (videos.isEmpty()) null else page + 1
                    )
                }
                is ApiResult.Error -> {
                    LoadResult.Error(result.throwable)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
