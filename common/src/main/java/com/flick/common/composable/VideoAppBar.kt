package com.flick.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flick.common.R

@Composable
fun VideoAppBar(
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()

    ) {
        Text(
            text = "구독",
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "추천",
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.home),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary)
        )
    }
}

@Preview(apiLevel = 34)
@Composable
fun VideoAppBarPreview() {
    VideoAppBar(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp))
}