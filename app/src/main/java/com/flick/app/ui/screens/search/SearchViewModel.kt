package com.flick.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flick.repository.VideoRepository
import com.flick.app.paging_source.SearchVideoPagingSource
import com.flick.app.ui.screens.show.VideoShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class SearchViewModel(
    private val repository: VideoRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<VideoShow>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                Pager(
                    config = PagingConfig(
                        pageSize = 15,
                        initialLoadSize = 15,
                        prefetchDistance = 5,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = { SearchVideoPagingSource(repository, query) }
                ).flow
            }
        }.cachedIn(viewModelScope)

    fun search(query: String) {
        _searchQuery.value = query
    }
}
