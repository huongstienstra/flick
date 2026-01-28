package com.flick.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.collectAsLazyPagingItems
import com.flick.app.navigation.Screen
import com.flick.app.ui.components.BottomNavigationBar
import com.flick.app.ui.dialogs.CommentsBottomSheet
import com.flick.app.ui.screens.profile.ProfileScreen
import com.flick.app.ui.screens.search.SearchScreen
import com.flick.app.ui.screens.search.SearchViewModel
import com.flick.app.ui.screens.show.HomeScreen
import com.flick.app.ui.screens.show.ShowViewModel
import com.flick.app.ui.screens.show.VideoShow

/**
 * Main app composable with bottom navigation and screens
 */
@Composable
fun FlickApp(
    mainViewModel: MainViewModel,
    showViewModel: ShowViewModel,
    searchViewModel: SearchViewModel,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var selectedVideo by remember { mutableStateOf<VideoShow?>(null) }

    val videos = showViewModel.videos.collectAsLazyPagingItems()
    val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentScreen.route,
                onNavigate = { screen -> currentScreen = screen }
            )
        }
    ) { paddingValues ->
        when (currentScreen) {
            Screen.Home -> {
                HomeScreen(
                    videos = videos,
                    onGetPlayer = {
                        showViewModel.getPlayer() as? ExoPlayer
                    },
                    onReleasePlayer = { player ->
                        showViewModel.releasePlayer(player)
                    },
                    onLikeVideo = { videoId ->
                        showViewModel.likeVideo(videoId)
                    },
                    onCommentClick = { video ->
                        selectedVideo = video
                        showCommentsSheet = true
                    },
                    onShareClick = { video ->
                        // Handle share
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            Screen.Search -> {
                SearchScreen(
                    searchResults = searchResults,
                    onSearch = { query ->
                        searchViewModel.search(query)
                    },
                    onVideoClick = { video ->
                        // Navigate to video detail or play video
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            Screen.Profile -> {
                ProfileScreen(
                    viewModel = mainViewModel,
                    onLoginClick = onNavigateToLogin,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }

    // Comments Bottom Sheet
    if (showCommentsSheet && selectedVideo != null) {
        val commentsPagingItems = showViewModel
            .getCommentsPagingData(selectedVideo!!.id)
            .collectAsLazyPagingItems()

        CommentsBottomSheet(
            comments = commentsPagingItems,
            totalComments = selectedVideo!!.commentCount,
            onPostComment = { content ->
                showViewModel.postComment(content)
            },
            onDismiss = {
                showCommentsSheet = false
                selectedVideo = null
            }
        )
    }
}
