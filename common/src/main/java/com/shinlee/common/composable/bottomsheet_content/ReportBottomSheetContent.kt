package com.shinlee.common.composable.bottomsheet_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinlee.common.R
import com.shinlee.common.composable.buttons.GradientButton
import com.shinlee.common.theme.Gray3Color

@Composable
fun ReportBottomSheetContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var showConfirmation by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf<String?>(null) }

    val reportReasons = listOf(
        "Violence, abuse, criminal exploitation",
        "Hate and harassment",
        "Suicide and self-harm",
        "Eating Disorders and Unhealthy Body Image",
        "Risky activities and challenges",
        "Nudity and sexual content"
    )

    if (showConfirmation) {
        ConfirmationScreen(
            //selectedReason = selectedReason,
            modifier = Modifier.wrapContentSize(),
            onBack = { showConfirmation = false },
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    } else {
        ReportReasonScreen(
            modifier = Modifier.wrapContentSize(),
            reportReasons = reportReasons,
            onReasonSelected = { reason ->
                selectedReason = reason
                showConfirmation = true
            },
            onDismiss = onDismiss
        )
    }
}

@Composable
fun ReportReasonScreen(
    modifier: Modifier,
    reportReasons: List<String>,
    onReasonSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        TitleBottomSheetDialog(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
            title = "Report",
            isNavigateUp = false,
            onDismiss = { onDismiss() },
            onBack = {})

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Why are you reporting this post?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        LazyColumn {
            items(reportReasons) { reason ->
                ReportReasonItem(reason = reason, onReasonSelected = onReasonSelected)
            }
        }
    }
}

@Composable
fun ConfirmationScreen(
    modifier: Modifier,
    onBack: () -> Unit, onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        TitleBottomSheetDialog(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
            title = "Report",
            isNavigateUp = true,
            onDismiss = { onDismiss() },
            onBack = { onBack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You're about to submit a report",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            text = "The following are not permitted:",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        GradientButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.btn_confirm),
            enable = true,
            onClick = { onConfirm() })
    }
}


@Composable
fun ReportReasonItem(reason: String, onReasonSelected: (String) -> Unit) {
    Row(modifier = Modifier
        .clickable {
            onReasonSelected(reason)
        }
        .fillMaxWidth()
        .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = reason, modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "Select",
            tint = Gray3Color
        )
    }
}

@Preview
@Composable
fun ReportReasonScreenPreview() {
    ReportReasonItem("Violence, abuse, criminal exploitation") {

    }
}

@Preview
@Composable
fun ReportScreen() {
    val reportReasons = listOf(
        "Violence, abuse, criminal exploitation",
        "Hate and harassment",
        "Suicide and self-harm",
        "Eating Disorders and Unhealthy Body Image",
        "Risky activities and challenges",
        "Nudity and sexual content"
    )
    ReportReasonScreen(modifier = Modifier.fillMaxSize(),
        reportReasons = reportReasons,
        onReasonSelected = { reason ->

        },
        onDismiss = {

        })
}

@Preview
@Composable
fun ConfirmationScreenPreview() {
    ConfirmationScreen(
        modifier = Modifier.fillMaxSize(),
        onBack = {},
        onConfirm = {},
        onDismiss = {})

}