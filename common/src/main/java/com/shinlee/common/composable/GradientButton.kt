package com.shinlee.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.theme.AppSpace
import com.shinlee.common.theme.Gray3Color
import com.shinlee.common.theme.Grey
import com.shinlee.common.theme.WhiteColor


@Composable
fun GradientButton(
    isActive: Boolean = true,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = if (isActive) {
        listOf(Color(0xFFFB5E3A), Color(0xFFE61DEC)) // Colors for active state
    } else {
        listOf(Grey, Grey) // Colors for inactive state
    }


    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .clickable { isActive }
            .fillMaxWidth(),
        shape = RoundedCornerShape(AppSpace.space8dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isActive) WhiteColor else Gray3Color,
                fontWeight = FontWeight.Normal,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}


@Preview
@Composable
fun GradientButtonPreview() {
    GradientButton(text = "Login with Email/ID", onClick = {})
}