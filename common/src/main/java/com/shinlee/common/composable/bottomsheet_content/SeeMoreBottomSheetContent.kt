package com.shinlee.common.composable.bottomsheet_content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.R
import com.shinlee.common.theme.AppSpace
import com.shinlee.common.theme.PinkColor

@Composable
fun VideoSeeMore(
    modifier: Modifier,
    onDescriptionClick: () -> Unit,
    onReportClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            DescriptionButton(
                onClick = onDescriptionClick,
                label = "Description",
                icon = R.drawable.ic_menu,
            )
            DescriptionButton(
                onClick = onReportClick,
                label = "Report",
                tintColor = PinkColor,
                icon = R.drawable.ic_info,
                modifier = Modifier
                    .padding(top = AppSpace.space12dp)
            )
        }
    }
}


@Composable
fun DescriptionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    tintColor: Color = Color.Black
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppSpace.space44dp)
            .clip(RoundedCornerShape(AppSpace.space8dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = AppSpace.space16dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "icon",
                tint = tintColor,
                modifier = Modifier
                    .size(AppSpace.space24dp)
                    .align(Alignment.CenterStart)
            )

            Text(
                text = label,
                fontSize = 14.sp,
                color = tintColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }
}


@Composable
fun BlackShape(modifier: Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {
        drawRoundedRectangle(
            color = Color.Black,
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
    }
}

fun DrawScope.drawRoundedRectangle(
    color: Color,
    cornerRadius: CornerRadius
) {
    drawRoundRect(
        color = color,
        cornerRadius = cornerRadius,
        size = Size(size.width, size.height)
    )
}


@Preview
@Composable
fun VideoSeeMorePreview() {
    VideoSeeMore(
        modifier = Modifier,
        onDescriptionClick = {

        },
        onReportClick = {
        }
    )
}