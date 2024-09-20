package com.shinlee.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R

@Composable
fun VideoInteractionButtons(
    modifier: Modifier,
    onShare: () -> Unit,
    onMessage: () -> Unit,
    onReaction: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconWithText(
            modifier = Modifier,
            icon = Icons.Outlined.Star,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(24.dp))
        IconWithText(
            modifier = Modifier,
            icon = Icons.Outlined.ThumbUp,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(24.dp))
        IconWithText(
            modifier = Modifier,
            icon = Icons.Outlined.FavoriteBorder,
            label = "48.5k",
            onClick = {}
        )
        Spacer(modifier = Modifier.height(24.dp))
        IconWithText(
            modifier = Modifier,
            icon = Icons.Outlined.Face,
            label = "48.5k",
            onClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))
        IconWithText(
            modifier = Modifier,
            icon = Icons.Outlined.MoreVert,
            label = null,
            onClick = {}
        )
    }
}

@Composable
fun IconWithText(
    modifier: Modifier,
    label: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onClick()
                },
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = R.color.text_primary)
        )
        if (!label.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = colorResource(id = R.color.text_primary)
            )
        }
    }

}

@Composable
@Preview
fun IconWithTextPreview() {
    VideoInteractionButtons(modifier = Modifier, onShare = {}, onMessage = {}, onReaction = {})
}

