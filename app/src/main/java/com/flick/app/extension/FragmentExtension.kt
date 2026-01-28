package com.flick.app.extension

import androidx.fragment.app.Fragment
import com.flick.common.R
import com.flick.common.dialogs.ErrorDialog
import com.flick.common.dialogs.OnClickListener

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

fun Fragment.requestLoginDialog(
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val dialog = ErrorDialog.newInstance(
        titleRes = R.string.dialog_request_login_title,
        messageRes = R.string.dialog_request_login_desc,
        positiveButtonTextRes = R.string.dialog_request_login_title_positive,
        negativeButtonTextRes = R.string.dialog_request_login_title_negative
    )
    dialog.showByTag(fragmentManager = childFragmentManager, "dialog_request_login", object : OnClickListener {
        override fun onNextClick() {
            onNext()
            dialog.dismissAllowingStateLoss()
        }

        override fun onCancelClick() {
            dialog.dismissAllowingStateLoss()
        }
    })
}