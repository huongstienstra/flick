package com.shinlee.common.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EasyLoginDivider(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Easy login with",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
        Divider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun EasyLoginDividerPreview() {
    EasyLoginDivider(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp))
}