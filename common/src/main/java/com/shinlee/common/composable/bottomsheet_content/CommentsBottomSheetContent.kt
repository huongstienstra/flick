import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R
import com.shinlee.common.composable.bottomsheet_content.TitleBottomSheetDialog
import com.shinlee.common.composable.comments.CommentData
import com.shinlee.common.composable.comments.CommentThreadLayout
import com.shinlee.common.composable.comments.ReplyData

@Composable
fun CommentsBottomSheetContent(
    modifier: Modifier,
    totalReplies: String,
    comments: List<CommentData>,
    onDismiss: () -> Unit,
    onAddCommentAction: () -> Unit = {},
    onDone: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(top = 16.dp, bottom = 24.dp)
    ) {

        TitleBottomSheetDialog(
            modifier = Modifier
                .fillMaxWidth().padding(end = 16.dp),
            title = "$totalReplies Comments",
            isNavigateUp = false,
            isNavigateRight = true,
            onDismiss = { onDismiss() },
            onBack = {}
        )

        CommentThreadLayout(
            comments = comments,
            onViewMoreReplies = { commentId -> /* Handle viewing more replies for this comment */ },
            onAddCommentAction = {
                onAddCommentAction()
            },
            onDone = {onDone(it)}
        )

    }
}


@Preview
@Composable
fun PreviewCircularStatComponent() {
    val comments = listOf(
        CommentData(
            id = "main1",
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = "27.6k",
            profileImage = R.drawable.avatar,
            showReplyButton = true,
            replies = listOf(
                ReplyData(
                    id = "reply1",
                    username = "cuckoo",
                    replyTo = "conheocon",
                    comment = "Thank you",
                    timestamp = "8-21",
                    likes = "27.6k",
                    profileImage = R.drawable.avatar
                )
            ),
            totalReplies = 100
        ),
        CommentData(
            id = "main2",
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = "27.6k",
            profileImage = R.drawable.avatar,
            showReplyButton = true
        ),
        CommentData(
            id = "main3",
            username = "Glucozo",
            comment = "Great, he must be the winner",
            timestamp = "8-21",
            likes = "27.6k",
            profileImage = R.drawable.avatar,
            showReplyButton = true
        )
    )
    CommentsBottomSheetContent(modifier = Modifier,"9.999", comments, onAddCommentAction = {}, onDismiss = {}, onDone = {})}