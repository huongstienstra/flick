import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.flick.common.composable.bottomsheet_content.TitleBottomSheetDialog
import com.flick.app.ui.screens.comment.CommentData
import com.flick.app.ui.screens.comment.CommentThreadLayout
import com.flick.app.ui.screens.show.ShowViewModel

@Composable
fun CommentsBottomSheetContent(
    modifier: Modifier,
    viewModel: ShowViewModel,
    totalOfComments: String,
    onDismiss: () -> Unit,
    commentPagingItems: LazyPagingItems<CommentData>,
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
            title = "$totalOfComments Comments",
            isNavigateUp = false,
            isNavigateRight = false,
            onDismiss = { onDismiss() },
            onBack = {}
        )

        CommentThreadLayout(
            viewModel = viewModel,
            commentPagingItems =  commentPagingItems,
            onViewMoreReplies = { commentId -> /* Handle viewing more replies for this comment */ },
            onAddCommentAction = { content ->

            },

        )

    }
}


//@Preview
//@Composable
//fun PreviewCircularStatComponent() {
//    val comments = listOf(
//        com.flick.app.ui.screens.comment.CommentData(
//            id = "main1",
//            username = "Glucozo",
//            comment = "Great, he must be the winner",
//            timestamp = "8-21",
//            likes = "27.6k",
//            profileImage = R.drawable.avatar,
//            showReplyButton = true,
//            replies = listOf(
//                com.flick.app.ui.screens.comment.ReplyData(
//                    id = "reply1",
//                    username = "cuckoo",
//                    replyTo = "conheocon",
//                    comment = "Thank you",
//                    timestamp = "8-21",
//                    likes = "27.6k",
//                    profileImage = R.drawable.avatar
//                )
//            ),
//            totalReplies = 100
//        ),
//        com.flick.app.ui.screens.comment.CommentData(
//            id = "main2",
//            username = "Glucozo",
//            comment = "Great, he must be the winner",
//            timestamp = "8-21",
//            likes = "27.6k",
//            profileImage = R.drawable.avatar,
//            showReplyButton = true
//        ),
//        com.flick.app.ui.screens.comment.CommentData(
//            id = "main3",
//            username = "Glucozo",
//            comment = "Great, he must be the winner",
//            timestamp = "8-21",
//            likes = "27.6k",
//            profileImage = R.drawable.avatar,
//            showReplyButton = true
//        )
//    )
    //CommentsBottomSheetContent(modifier = Modifier,"9.999", comments, onAddCommentAction = {}, onDismiss = {}, onDone = {})}