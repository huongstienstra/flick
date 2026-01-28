package com.flick.common.composable.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flick.common.R
import com.flick.common.theme.AppSpace
import com.flick.common.theme.BlackColor
import com.flick.common.theme.GrayColor
import com.flick.common.theme.WhiteColor

@Composable
fun SocialButtonHorizontal(
    modifier: Modifier = Modifier,
    icon: Int,
    value: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(AppSpace.space48dp)
            .fillMaxWidth(),
        onClick = {
            onClick()
        },
        border = BorderStroke(1.dp, GrayColor),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(AppSpace.space8dp),
        colors = ButtonDefaults.buttonColors(Color.White),

        ) {
        Row(
            modifier = Modifier
                .background(WhiteColor)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(start = AppSpace.space16dp)) {
                Image(
                    modifier = Modifier.size(AppSpace.space24dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = icon),
                    contentDescription = "Custom Drawable Icon",
                )

            }

            Text(
                modifier = Modifier.weight(1f),
                text = value,
                color = BlackColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun SocialButtonHorizontalPreview() {
    SocialButtonHorizontal(
        modifier = Modifier.fillMaxWidth(),
        icon = R.drawable.ic_google_register,
        value = "구글"
    ) {

    }
}