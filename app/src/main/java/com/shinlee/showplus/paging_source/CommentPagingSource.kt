package com.shinlee.showplus.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shinlee.network.ApiResult
import com.shinlee.repository.VideoRepository
import com.shinlee.showplus.ui.screens.comment.CommentData
import com.shinlee.showplus.ui.screens.show.mapping.toCommentDataList

class CommentPagingSource(
    private val repository: VideoRepository,
    private val videoId: Long,
) : PagingSource<Int, CommentData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentData> {
        val page = params.key ?: 1
        return try {

            val response = repository.getComments(videoId, page = page, pageSize = params.loadSize)
            when (response) {
                is ApiResult.Success -> {
                    val comments = response.data.toCommentDataList()
                    LoadResult.Page(
                        data = comments,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (comments.isEmpty()) null else page + 1
                    )
                }

                is ApiResult.Error -> {
                    LoadResult.Error(response.throwable)
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CommentData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}