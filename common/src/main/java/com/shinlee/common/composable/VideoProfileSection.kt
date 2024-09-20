package com.shinlee.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shinlee.common.R

@Composable
fun VideoProfileSection(
    modifier: Modifier,
    avatarURL: String,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(48.dp) // Set the size
                    .clip(CircleShape) // Clip the image to a circle
                    .background(colorResource(id = R.color.background_color)) // Background color if needed
            ) {
                AsyncImage(
                    model = avatarURL,
                    contentDescription = null,
                )
            }

            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = "test1234",
                color = colorResource(id = R.color.text_primary),
                style = MaterialTheme.typography.bodyMedium
            )

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Button(
                    onClick = { /* Handle subscribe */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_active)
                    )
                ) {
                    Text(
                        text = "구독하기",
                        color = colorResource(id = R.color.button_inactive)
                    )
                }

                Button(
                    onClick = { /* Handle subscribe */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_active)
                    )
                ) {
                    Text(
                        text = "구독하기",
                        color = colorResource(id = R.color.button_inactive)
                    )
                }
            }
        }
        // Post Text
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "DJ둠치둠치가 돌아왔다\n모두들 광란의 디깅을 준비하세요!!",
            color = colorResource(id = R.color.text_primary),
            style = MaterialTheme.typography.bodyLarge,
        )
        // Tags Section
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "#DJ #버스킹 #공연 #부천핫썹",
            color = colorResource(id = R.color.text_primary),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun ProfileSectionPreview() {
    VideoProfileSection(
        modifier = Modifier,
        avatarURL = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
    )
}