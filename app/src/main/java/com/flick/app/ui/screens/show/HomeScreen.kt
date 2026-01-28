package com.flick.app.ui.screens.show

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.flick.app.ui.screens.show.composable.VideoPlayer
import timber.log.Timber

/**
 * Home screen with vertical video feed (TikTok-style)
 *
 * Optimizations:
 * - Pre-renders next video surface for instant playback on swipe
 * - Uses adaptive buffering based on network conditions
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    videos: LazyPagingItems<VideoShow>,
    onGetPlayer: suspend () -> ExoPlayer?,
    onGetPlayerForVideo: (String) -> ExoPlayer,
    onReleasePlayer: (ExoPlayer) -> Unit,
    onPreloadVideo: (String) -> Unit,
    onLikeVideo: (Long) -> Unit,
    onCommentClick: (VideoShow) -> Unit,
    onShareClick: (VideoShow) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { videos.itemCount }
    )

    var currentPage by remember { mutableIntStateOf(0) }
    var currentPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var currentVideoUrl by remember { mutableStateOf<String?>(null) }

    // Track page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Timber.d("Page changed to: $page")
            currentPage = page
        }
    }

    // Get player for current page - uses preloaded player if available
    LaunchedEffect(currentPage, videos.itemCount) {
        // Release previous player
        currentPlayer?.let { player ->
            player.stop()
            onReleasePlayer(player)
        }

        // Get video URL for current page
        val video = if (currentPage < videos.itemCount) videos[currentPage] else null
        val videoUrl = video?.videoUrl

        if (videoUrl != null && videoUrl.isNotBlank()) {
            currentVideoUrl = videoUrl
            // Try to get preloaded player, falls back to new player
            currentPlayer = onGetPlayerForVideo(videoUrl)
        } else {
            currentVideoUrl = null
            currentPlayer = onGetPlayer()
        }
    }

    // Preload next video when page settles (after swipe animation completes)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { settledPage ->
            Timber.d("Page settled at: $settledPage")
            val nextIndex = settledPage + 1
            if (nextIndex < videos.itemCount) {
                videos[nextIndex]?.videoUrl?.let { url ->
                    if (url.isNotBlank()) {
                        Timber.d("Preloading next video: $url")
                        onPreloadVideo(url)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (videos.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { page ->
                val video = videos[page]
                val isCurrentPage = page == currentPage

                video?.let {
                    VideoPageContent(
                        video = it,
                        player = if (isCurrentPage) currentPlayer else null,
                        isPlaying = isCurrentPage,
                        onLikeClick = { onLikeVideo(it.id) },
                        onCommentClick = { onCommentClick(it) },
                        onShareClick = { onShareClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoPageContent(
    video: VideoShow,
    player: ExoPlayer?,
    isPlaying: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var isLiked by remember(video.id) { mutableStateOf(video.isFavourite) }
    var likeCount by remember(video.id) { mutableIntStateOf(video.voteCount) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Video Player with thumbnail for loading state
        VideoPlayer(
            videoUrl = video.videoUrl ?: "",
            thumbnailUrl = video.thumbnailUrl,
            player = player,
            isPlaying = isPlaying,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Left side - User info and description
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 80.dp, bottom = 16.dp)
            ) {
                // User info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = video.profilePhoto,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = video.profileNickname,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = video.description ?: "",
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 3
                )
            }

            // Right side - Action buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Like button
                ActionButton(
                    icon = {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    count = likeCount,
                    onClick = {
                        isLiked = !isLiked
                        likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                        onLikeClick()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Comment button
                ActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    count = video.commentCount,
                    onClick = onCommentClick
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Share button
                ActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    count = 0,
                    onClick = onShareClick
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: @Composable () -> Unit,
    count: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        icon()
        if (count > 0) {
            Text(
                text = formatCount(count),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
