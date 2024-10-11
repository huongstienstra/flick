package com.shinlee.common.composable.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R
import com.shinlee.common.theme.AntiFlashWhite
import kotlinx.coroutines.delay

@Composable
fun AddCommentSection(
    modifier: Modifier,
    onAddComment: () -> Unit,
    onDone: (String) -> Unit,
    enableInputText: Boolean = false
) {
    var commentText by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(enableInputText) {
        if (enableInputText) {
            delay(300) // Short delay to ensure the view is ready
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier.clickable {
            onAddComment()
        }
    ) {
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            if (enableInputText) {
                BorderedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = "Add comment...",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .focusRequester(focusRequester)
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(AntiFlashWhite),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        text = "Add a comment...",
                        color = Color.Gray
                    )
                }
            }

        }
        CommentInputActions(
            onMentionClick = { /* Handle mention click */ },
            onEmojiClick = { /* Handle emoji click */ },
            onSendClick = {
                onDone(commentText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun CommentInputActions(
    onMentionClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onMentionClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_at),
                contentDescription = "Mention",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onEmojiClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_emoji),
                contentDescription = "Emoji",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .width(40.dp)
                .height(25.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFFC0CB))
                .clickable(onClick = onSendClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_up),
                contentDescription = "Send",
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
fun BorderedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(placeholder, color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BorderedTextFieldPreview() {
    BorderedTextField(
        value = "Sample Text",
        onValueChange = {},
        placeholder = "Placeholder",
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun AddCommentSectionPreview() {
    AddCommentSection(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        enableInputText = false, onAddComment = {

        }, onDone = {

        })
}

@Preview(showBackground = true)
@Composable
fun AddCommentActionPreview() {
    CommentInputActions(
        onMentionClick = { /* Handle mention click */ },
        onEmojiClick = { /* Handle emoji click */ },
        onSendClick = { /* Handle send click */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}
