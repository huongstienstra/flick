import com.shinlee.showplus.ui.screens.show.dialogs.InputCommentDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shinlee.showplus.ui.screens.comment.CommentData
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.show.ShowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_VIDEO_ID = "video_id"
        private const val ARG_TOTAL_COMMENTS = "total_of_comments"

        fun newInstance(videoId: Long, totalOfComments: Int): CommentBottomSheet {
            val fragment = CommentBottomSheet()
            val args = Bundle()
            args.putLong(ARG_VIDEO_ID, videoId)
            args.putInt(ARG_TOTAL_COMMENTS, totalOfComments)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: ShowViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.comment_bottomsheet, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.5f)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val initialHeight = (screenHeight * 0.75).toInt()

        bottomSheet.background = null

       behavior.maxWidth = initialHeight
       behavior.state = BottomSheetBehavior.STATE_EXPANDED

        var videoId: Long = -1
        var totalOfComments = -1
        arguments?.let {
            videoId = it.getLong(ARG_VIDEO_ID, -1)
            totalOfComments = it.getInt(ARG_TOTAL_COMMENTS, 0)
        }

        viewModel.currentVideoId = videoId

        view.findViewById<ComposeView>(R.id.compose_view).setContent {

            val commentPagingItems: LazyPagingItems<CommentData> =
                viewModel.getCommentsPagingData(viewModel.currentVideoId).collectAsLazyPagingItems()

            CommentsBottomSheetContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(650.dp),
                viewModel = viewModel,
                commentPagingItems = commentPagingItems,
                totalOfComments = totalOfComments.toString(),
                onDismiss = {
                    dismissAllowingStateLoss()
                },
            )

        }
    }

    fun showByTag(manager: FragmentManager) {
        super.show(manager, CommentBottomSheet::class.java.name)
    }

}