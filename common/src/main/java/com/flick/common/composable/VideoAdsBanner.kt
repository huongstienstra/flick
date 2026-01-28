package com.flick.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flick.common.R

@Composable
fun VideoAdBanner(modifier: Modifier) {
    Row(
        modifier = modifier
            .background(colorResource(id = R.color.overlay_color))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "지금 바로 쇼플러스의 경연에 참가해보세요.",
            color = colorResource(id = R.color.text_primary),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = "지금 바로",
            color = colorResource(id = R.color.text_primary),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
fun VideoAdBannerPreview() {
    VideoAdBanner(modifier = Modifier.fillMaxWidth())
}