package com.shinlee.showplus.ui.screens.show.dialogs

import CommentsBottomSheetContent
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shinlee.common.composable.comments.CommentData
import com.shinlee.common.composable.comments.ReplyData
import com.shinlee.showplus.R

class CommentsBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.comment_bottomsheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = false

        bottomSheet.background = null


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

        // Set initial height (e.g., 75% of screen height)
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val initialHeight = (screenHeight * 0.75).toInt()

        behavior.peekHeight = initialHeight
        behavior.state = BottomSheetBehavior.STATE_EXPANDED


        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            val comments = listOf(
                CommentData(
                    id = "main1",
                    username = "Glucozo",
                    comment = "Great, he must be the winner",
                    timestamp = "8-21",
                    likes = "27.6k",
                    profileImage = com.shinlee.common.R.drawable.avatar,
                    showReplyButton = true,
                    replies = listOf(
                        ReplyData(
                            id = "reply1",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
                        ),
                        ReplyData(
                            id = "reply2",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
                        ),
                        ReplyData(
                            id = "reply3",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
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
                    profileImage = com.shinlee.common.R.drawable.avatar,
                    showReplyButton = true
                ),
                CommentData(
                    id = "main3",
                    username = "Glucozo",
                    comment = "Great, he must be the winner",
                    timestamp = "8-21",
                    likes = "27.6k",
                    profileImage = com.shinlee.common.R.drawable.avatar,
                    showReplyButton = true
                ),
                CommentData(
                    id = "main4",
                    username = "Glucozo",
                    comment = "Great, he must be the winner",
                    timestamp = "8-21",
                    likes = "27.6k",
                    profileImage = com.shinlee.common.R.drawable.avatar,
                    showReplyButton = true
                ),
                CommentData(
                    id = "main5",
                    username = "Glucozo",
                    comment = "Great, he must be the winner",
                    timestamp = "8-21",
                    likes = "27.6k",
                    profileImage = com.shinlee.common.R.drawable.avatar,
                    showReplyButton = true,
                    replies = listOf(
                        ReplyData(
                            id = "reply1",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
                        ),
                        ReplyData(
                            id = "reply2",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
                        ),
                        ReplyData(
                            id = "reply3",
                            username = "cuckoo",
                            replyTo = "conheocon",
                            comment = "Thank you",
                            timestamp = "8-21",
                            likes = "27.6k",
                            profileImage = com.shinlee.common.R.drawable.avatar
                        )
                    ),
                    totalReplies = 100
                )
            )

            CommentsBottomSheetContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(650.dp),
                "99",
                comments,
                onAddCommentAction = {
                    InputCommentDialog().showByTag(childFragmentManager)
                },
                onDismiss = {
                    dismissAllowingStateLoss()
                },
                onDone = {
                    dismissAllowingStateLoss()
                }
            )

        }
    }

    fun showByTag(manager: FragmentManager) {
        super.show(manager, CommentsBottomSheet::class.java.name)
    }

}