package com.shinlee.showplus.extension

import androidx.fragment.app.Fragment
import com.shinlee.common.dialogs.ErrorDialog
import com.shinlee.common.dialogs.OnClickListener

fun Fragment.showExitConfirmationDialog(
    title: Int,
    desc: Int,
    positiveBtn: Int,
    negativeBtn: Int,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val dialog = ErrorDialog.newInstance(
        titleRes = title,
        messageRes = desc,
        positiveButtonTextRes = positiveBtn,
        negativeButtonTextRes = negativeBtn
    )

    dialog.showByTag(fragmentManager = childFragmentManager, "exit_confirmation_dialog", object :
        OnClickListener {
        override fun onNextClick() {
            dialog.dismissAllowingStateLoss()
            onNext()
        }

        override fun onCancelClick() {
            dialog.dismissAllowingStateLoss()
            onCancel()
        }
    })
}