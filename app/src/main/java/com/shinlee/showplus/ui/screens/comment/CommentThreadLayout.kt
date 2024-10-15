package com.shinlee.showplus.ui.screens.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.shinlee.common.R
import com.shinlee.common.composable.comments.AddCommentSection
import com.shinlee.showplus.utils.NumberFormatter
import com.shinlee.showplus.utils.TimeUtils

@Composable
fun CommentThreadLayout(
    commentPagingItems: LazyPagingItems<CommentData>,
    onViewMoreReplies: (Int) -> Unit,
    onAddCommentAction: () -> Unit = {},
    onDone: (String) -> Unit
) {
    var addCommentSectionHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = addCommentSectionHeight)
        ) {
            items(commentPagingItems.itemCount) { index ->
                val comment = commentPagingItems[index]
                comment?.let {
                    CommentWithReplies(
                        comment = it,
                        onViewMoreReplies = onViewMoreReplies
                    )
                }
            }

            commentPagingItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            // LoadingItem()
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            //LoadingItem()
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
//                        val e = comments.loadState.refresh as LoadState.Error
//                        item {
//                            ErrorItem(
//                                message = e.error.localizedMessage ?: "Unknown error",
//                                onRetryClick = { comments.retry() }
//                            )
//                        }
                    }

                    loadState.append is LoadState.Error -> {
//                        val e = comments.loadState.append as LoadState.Error
//                        item {
//                            ErrorItem(
//                                message = e.error.localizedMessage ?: "Unknown error",
//                                onRetryClick = { comments.retry() }
//                            )
//                        }
                    }
                }
            }

        }

        AddCommentSection(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .onGloballyPositioned { coordinates ->
                    addCommentSectionHeight = with(density) { coordinates.size.height.toDp() }
                },
            enableInputText = false,
            onAddComment = {
                onAddCommentAction()
            },
            onDone = {
                onDone(it)
            }
        )
    }
}

@Composable
fun CommentItem(
    username: String,
    comment: String,
    timestamp: String,
    likes: String,
    profileImage: String,
    showReplyButton: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        AsyncImage(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
            model = profileImage,
            placeholder = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = username.ifEmpty { "---" },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = comment,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = TimeUtils.getTimeAgo(timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                if (showReplyButton) {
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Reply",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_kakao_register),
                    contentDescription = "Emoji Reactions",
                    modifier = Modifier.height(16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.Bottom) {
                    IconButton(
                        modifier = Modifier.size(16.dp),
                        onClick = { /* Handle like action */ }) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = likes,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ReplyItem(
    username: String,
    replyTo: String,
    comment: String,
    timestamp: String,
    likes: String,
    profileImage: String,
    showReplyButton: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            model = profileImage,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.avatar),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = " > $replyTo",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = comment,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = TimeUtils.getTimeAgo(timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                if (showReplyButton) {
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Reply",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_kakao_register),
                    contentDescription = "Emoji Reactions",
                    modifier = Modifier.height(16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.Bottom) {
                    IconButton(
                        modifier = Modifier.size(16.dp),
                        onClick = { /* Handle like action */ }) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = likes,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MainComment(
    username: String,
    comment: String,
    timestamp: String,
    likes: String,
    profileImage: String,
    showReplyButton: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CommentItem(
            username,
            comment,
            timestamp,
            likes,
            profileImage,
            showReplyButton
        )
    }

}

@Composable
fun ViewMoreRepliesItem(
    modifier: Modifier,
    replyCount: Int,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.LightGray.copy(alpha = 0.5f),
            thickness = 2.dp,
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = "View $replyCount replies",
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "Expand replies",
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .padding(start = 8.dp)
        )
    }

}

@Composable
fun ReplyComment(
    username: String,
    replyTo: String,
    comment: String,
    timestamp: String,
    likes: String,
    profileImage: String,
    showReplyButton: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        ReplyItem(
            username,
            replyTo,
            comment,
            timestamp,
            likes,
            profileImage = profileImage,
            showReplyButton = showReplyButton
        )
    }
}

@Composable
fun CommentWithReplies(
    comment: CommentData,
    onViewMoreReplies: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        MainComment(
            username = comment.username,
            comment = comment.comment,
            timestamp = comment.timestamp,
            likes = NumberFormatter.formatSocialCount(comment.likes),
            profileImage = comment.profileImage,
            showReplyButton = true
        )

        if (isExpanded) {
            comment.replies.forEach { reply ->
                ReplyComment(
                    username = reply.username,
                    replyTo = reply.replyTo,
                    comment = reply.comment,
                    timestamp = reply.timestamp,
                    likes = NumberFormatter.formatSocialCount(comment.likes),
                    profileImage = reply.profileImage,
                    showReplyButton = false
                )
            }
        } else {
            comment.replies.take(1).forEach { reply ->
                ReplyComment(
                    username = reply.username,
                    replyTo = reply.replyTo,
                    comment = reply.comment,
                    timestamp = reply.timestamp,
                    likes = NumberFormatter.formatSocialCount(comment.likes),
                    profileImage = reply.profileImage,
                    showReplyButton = false
                )
            }
        }

        if (comment.totalReplies > 1) {
            ViewMoreRepliesItem(
                modifier = Modifier.padding(start = 36.dp, top = 8.dp, bottom = 8.dp),
                replyCount = comment.totalReplies,
                onClick = {
                    isExpanded = true
                    onViewMoreReplies(comment.id)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentWithRepliesPreview() {
    val comment =
        CommentData(
            id = 1,
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = 27,
            profileImage = "",
            showReplyButton = true,
            replies = listOf(
                ReplyData(
                    id = 1,
                    username = "cuckoo",
                    replyTo = "conheocon",
                    comment = "Thank you",
                    timestamp = "8-21",
                    likes = 26,
                    profileImage = ""
                )
            ),
            totalReplies = 100
        )
    CommentWithReplies(
        comment
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun CommentThreadLayoutPreview() {
    val comments = listOf(
        CommentData(
            id = 1,
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = 27,
            profileImage = "",
            showReplyButton = true,
            replies = listOf(
                ReplyData(
                    id = 1,
                    username = "cuckoo",
                    replyTo = "conheocon",
                    comment = "Thank you",
                    timestamp = "8-21",
                    likes = 26,
                    profileImage = ""
                )
            ),
            totalReplies = 100
        ),
        CommentData(
            id = 2,
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = 27,
            profileImage = "",
            showReplyButton = true
        ),
        CommentData(
            id = 3,
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = 27,
            profileImage = "",
            showReplyButton = true
        )
    )

//    CommentThreadLayout(
//        comments = comments,
//        onViewMoreReplies = { commentId -> /* Handle viewing more replies for this comment */ },
//        onDone = {}
//    )
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    MainComment(
        username = "Song Hye Kyo 🦄",
        comment = "Thank you",
        timestamp = "8-21",
        likes = "27.6k",
        profileImage = "",
        showReplyButton = true
    )
}

@Preview(showBackground = true)
@Composable
fun ReplyCommentPreview() {
    ReplyComment(
        username = "Song Hye Kyo 🦄",
        replyTo = "conheocon",
        comment = "Thank you",
        timestamp = "8-21",
        likes = "27.6k",
        profileImage = "",
        showReplyButton = false
    )
}

@Preview(showBackground = true)
@Composable
fun ViewMoreRepliesPreview() {
    ViewMoreRepliesItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), replyCount = 99
    ) {

    }
}