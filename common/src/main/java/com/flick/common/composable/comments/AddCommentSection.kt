package com.flick.common.composable.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flick.common.R

@Composable
fun AddCommentSection(
    modifier: Modifier,
    onAddComment: (String) -> Unit,
) {
    var commentText by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.clickable {
            focusRequester.requestFocus()
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

            BorderedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = "Add comment...",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ).copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

        }
        CommentInputActions(
            onMentionClick = { /* Handle mention click */ },
            onEmojiClick = { /* Handle emoji click */ },
            onSendClick = {
                onAddComment(commentText)
                focusManager.clearFocus()
                commentText = ""
            },
            isEnabledSend = commentText.isNotEmpty(),
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
    isEnabledSend: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColorButtonSend = if (isEnabledSend) Color(0xFFFFC0CB) else Color(0xFFFFE0E0)

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
                .background(backgroundColorButtonSend)
                .clickable(onClick = onSendClick, enabled = isEnabledSend),
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
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
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
        onAddComment = {

        })
}

@Preview(showBackground = true)
@Composable
fun AddCommentActionPreview() {
    CommentInputActions(
        onMentionClick = { /* Handle mention click */ },
        onEmojiClick = { /* Handle emoji click */ },
        onSendClick = { /* Handle send click */ },
        isEnabledSend = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}
