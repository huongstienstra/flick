package com.shinlee.repository.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shinlee.network.ApiResult
import com.shinlee.repository.VideoRepository
import com.shinlee.repository.model.VideoShow
import com.shinlee.repository.mapping.toVideoShowList


class VideoPagingSource(
    private val repository: VideoRepository
) : PagingSource<Int, VideoShow>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoShow> {
        val page = params.key ?: 1
        return try {
            val response = repository.getVideos(page = page, pageSize = params.loadSize)
            if (response is ApiResult.Success) {
                val data = response.data.toVideoShowList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error((response as ApiResult.Error).throwable)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideoShow>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
