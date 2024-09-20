package com.shinlee.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageInfo
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R

@Composable
fun VerticalVideoPage(
    videoUri: String,
    modifier: Modifier,
    pagerState: PagerState,
    pageIndex: Int
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Video Player
//        VideoPlayer(
//            videoUri = videoUri, // Replace with your video URL
//            modifier = Modifier.fillMaxSize(),
//            thumbnailUrl = "",
//            pagerState = pagerState,
//            pageIndex = pageIndex
//        )

        VideoPlayer2(
            video = videoUri,
            thumbnailUrl = "" ,
            pagerState = pagerState,
            pageIndex = pageIndex,
            onSingleTap = {
                // Handle single tap
            },
            onDoubleTap = { expo, index -> },
            onVideoDispose =  {
                // Handle video dispose
            }, onVideoGoBackground = {
                // Handle video go background
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            // Top Bar
            VideoAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Video Ad Banner
            VideoAdBanner(modifier = Modifier.fillMaxWidth())

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                // Profile and Interaction Section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    VideoProfileSection(
                        modifier = Modifier,
                        avatarURL = "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Interaction Buttons
                VideoInteractionButtons(
                    modifier = Modifier.padding(bottom = 48.dp),
                    onShare = {},
                    onMessage = {},
                    onReaction = {})
            }
        }
    }
}

@Preview
@Composable
fun VerticalVideoPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ) {
//        VerticalVideoPage(
//            modifier = Modifier.fillMaxSize(),
//            videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
//        )
    }
}
