package com.shinlee.common.composable.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommentInputDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCommentSubmit: (String) -> Unit
) {
    if (isVisible) {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFF1E1E1E))
                ) {

                    CommentInputField(
                        onCommentSubmit = onCommentSubmit,
                        focusRequester = focusRequester
                    )
                    EmojiSelector()
                    EmojiSelector()
                }
            }

            LaunchedEffect(isVisible) {
                if (isVisible) {
                    coroutineScope.launch {
                        delay(100)
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}

@Composable
fun CommentInputField(
    onCommentSubmit: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var commentText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Add a comment...", color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .background(Color(0xFF2C2C2C), RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            trailingIcon = {
                IconButton(onClick = {
                    if (commentText.isNotBlank()) {
                        onCommentSubmit(commentText)
                        commentText = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        )
    }
}

@Composable
fun EmojiSelector() {
    // Implement emoji selector here
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Add emoji buttons here
        // Example:
        listOf("❤️", "😂", "🎉", "😢", "😮", "😊").forEach { emoji ->
            Text(
                text = emoji,
                modifier = Modifier.clickable { /* Handle emoji selection */ }
            )
        }
    }
}

@Composable
fun ActionButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Add action buttons here
        // Example:
//        listOf(
//            Icons.Default.Mood to "Emoji",
//            Icons.Default.AttachFile to "Attach",
//            Icons.Default.Mic to "Voice",
//            Icons.Default.Settings to "Settings"
//        ).forEach { (icon, description) ->
//            IconButton(onClick = { /* Handle action */ }) {
//                Icon(icon, contentDescription = description, tint = Color.White)
//            }
//        }
    }
}