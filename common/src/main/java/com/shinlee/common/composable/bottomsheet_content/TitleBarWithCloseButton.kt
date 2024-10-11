package com.shinlee.common.composable.bottomsheet_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.theme.Gray3Color


@Composable
fun TitleBottomSheetDialog(
    modifier: Modifier,
    title: String,
    isNavigateUp: Boolean = false,
    isNavigateRight: Boolean = true,
    onDismiss: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = modifier
    ) {

        if (isNavigateUp) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Gray3Color,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickable(onClick = {
                        onBack()
                    })
            )
        }

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        if(isNavigateRight) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Gray3Color,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = onDismiss)
            )
        }

    }
}

@Preview
@Composable
fun PreviewTitleBarWithCloseButton() {
    TitleBottomSheetDialog(
        modifier = Modifier.fillMaxWidth(),
        title = "Description",
        isNavigateUp = true,
        onBack = { /* Handle back */ },
        onDismiss = { /* Handle dismiss */ }
    )
}