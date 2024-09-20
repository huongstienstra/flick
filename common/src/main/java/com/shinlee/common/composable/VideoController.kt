package com.shinlee.common.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoController(
    modifier: Modifier,
    currentPosition: Long,
    duration: Long,
    onSeekTo: (Long) -> Unit,
    thumbnailUrl: String
) {
    // Seek bar state
    var sliderPosition by remember { mutableStateOf(currentPosition.toFloat()) }

    val interactionSource = remember { MutableInteractionSource() }

    // Update the slider's position whenever the currentPosition changes
    LaunchedEffect(currentPosition) {
        sliderPosition = currentPosition.toFloat() / duration.toFloat()
    }

    Column(
        modifier = modifier
    ) {
        // Thumbnail preview
        Image(
            painter = rememberAsyncImagePainter(thumbnailUrl),
            contentDescription = null,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentScale = ContentScale.Crop
        )

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
            },
            onValueChangeFinished = {
                // Convert the slider's position to the video position
                val seekPosition = (sliderPosition * duration).toLong()
                onSeekTo(seekPosition)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Red,
                inactiveTrackColor = Color.Gray,
                //thumbColor = Color.White,
            ),
            thumb = {
                // Custom thumb implementation
                SliderDefaults.Thumb(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    thumbSize = DpSize(16.dp, 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White
                    )
                )
//                Box(
//                    modifier = Modifier
//                        .size(16.dp) // Size of the custom thumb
//                        .background(Color.White, CircleShape) // Custom shape and color
//                )
            },

            track = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray)// Customize the track height here
                )
            },

            interactionSource = interactionSource // Pass interactionSource to track user interactions
        )

        // Current time
        Text(
            text = "0:11",
            color = Color.White,
            modifier = Modifier.align(Alignment.End)
        )
    }
}