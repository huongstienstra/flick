package com.flick.common.composable.buttons


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flick.common.theme.AppSpace

@Composable
fun OutlinedCustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(AppSpace.space8dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = ButtonDefaults.outlinedButtonBorder()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

@Preview
@Composable
fun OutlinedCustomButtonPreview() {
    OutlinedCustomButton(
        modifier = Modifier.fillMaxWidth(),
        text = "SignUp",
        onClick = {}
    )
}