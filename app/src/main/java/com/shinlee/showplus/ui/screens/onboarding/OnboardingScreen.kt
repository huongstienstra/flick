package com.shinlee.showplus.ui.screens.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.shinlee.showplus.ui.components.ButtonComponent


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(modifier: Modifier, onNextScreen: () -> Unit) {

    val pagerState = rememberPagerState(
        pageCount = kidsList.size,
        initialPage = 2
    )

    LaunchedEffect(Unit) {
        pagerState.animateScrollToPage(
            page = (pagerState.currentPage + 1) % (pagerState.pageCount),
            animationSpec = tween(0)
        )
    }

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
        ) { page ->
            Card(
            ) {
                val newKids = kidsList[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(
                            id = newKids.imgUri
                        ),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(20.dp, 20.dp, 0.dp, 150.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = newKids.title,
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = newKids.desc,
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }
            }

        }

    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        HorizontalPagerIndicator(
            pagerState = pagerState, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(100.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        ButtonComponent(
            value = "Next",
            onButtonClicked = {
                onNextScreen()
            },
            isEnabled = true
        )
    }


}