package com.flick.app.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.flick.app.ui.screens.show.VideoShow

/**
 * Search screen with search bar and video grid
 */
@Composable
fun SearchScreen(
    searchResults: LazyPagingItems<VideoShow>,
    onSearch: (String) -> Unit,
    onVideoClick: (VideoShow) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                focusManager.clearFocus()
                onSearch(searchQuery)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Video grid
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                searchResults.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                searchResults.itemCount == 0 && searchQuery.isNotEmpty() -> {
                    Text(
                        text = "No videos found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                searchResults.itemCount == 0 -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Search for videos",
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    VideoGrid(
                        videos = searchResults,
                        onVideoClick = onVideoClick
                    )
                }
            }

            // Loading more indicator
            if (searchResults.loadState.append is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search videos...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.LightGray
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
private fun VideoGrid(
    videos: LazyPagingItems<VideoShow>,
    onVideoClick: (VideoShow) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(videos.itemCount) { index ->
            videos[index]?.let { video ->
                VideoGridItem(
                    video = video,
                    onClick = { onVideoClick(video) }
                )
            }
        }
    }
}

@Composable
private fun VideoGridItem(
    video: VideoShow,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // User name overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Text(
                    text = video.profileNickname,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }
    }
}
