package com.flick.common.composable.bottomsheet_content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flick.common.theme.AppSpace
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun DescriptionBottomSheetContent(
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        TitleBottomSheetDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            title = "Description",
            isNavigateUp = false,
            onDismiss = { onDismiss() },
            onBack = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "This is related to the video DJ Doomchi Shrew is back.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            Text(
                text = "#DJ ",
                color = Color.Blue,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "#Contest ",
                color = Color.Blue,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "#Music",
                color = Color.Blue,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircularStatComponent(
                value = "4.2k",
                label = "Likes",
                modifier = Modifier.padding(8.dp)
            )

            CircularStatComponent(
                value = "4.2m",
                label = "Views",
                modifier = Modifier.padding(8.dp)
            )
            CircularStatComponent(
                value = "12/30\n" +
                        "2024",
                label = "Date Joined",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CircularStatComponent(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.width / 2 - 1.dp.toPx()

                drawCircle(
                    color = Color.LightGray,
                    center = Offset(centerX, centerY),
                    radius = radius,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(AppSpace.space8dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun PreviewCircularStatComponent() {
    DescriptionBottomSheetContent() {

    }
}