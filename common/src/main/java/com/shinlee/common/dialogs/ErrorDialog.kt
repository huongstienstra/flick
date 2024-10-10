package com.shinlee.common.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shinlee.common.R

interface OnClickListener {
    fun onNextClick()
    fun onCancelClick()
}

class ErrorDialog : DialogFragment() {

    private var onClickListener: OnClickListener? = null

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "positiveButtonText"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "negativeButtonText"

        fun newInstance(
            titleRes: Int,
            messageRes: Int,
            positiveButtonTextRes: Int,
            negativeButtonTextRes: Int
        ): ErrorDialog {
            val fragment = ErrorDialog()
            val args = Bundle()
            args.putInt(ARG_TITLE, titleRes)
            args.putInt(ARG_MESSAGE, messageRes)
            args.putInt(ARG_POSITIVE_BUTTON_TEXT, positiveButtonTextRes)
            args.putInt(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonTextRes)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView: AppCompatTextView = view.findViewById(R.id.titleTextView)
        val messageTextView: AppCompatTextView = view.findViewById(R.id.messageTextView)
        val cancelButton: AppCompatTextView = view.findViewById(R.id.cancelButton)
        val nextButton: AppCompatTextView = view.findViewById(R.id.nextButton)

        arguments?.let { args ->
            titleTextView.setText(args.getInt(ARG_TITLE))
            messageTextView.setText(args.getInt(ARG_MESSAGE))
            cancelButton.setText(args.getInt(ARG_NEGATIVE_BUTTON_TEXT))
            nextButton.setText(args.getInt(ARG_POSITIVE_BUTTON_TEXT))

        }

        cancelButton.setOnClickListener {
            onClickListener?.onCancelClick()
            dismissAllowingStateLoss()
        }

        nextButton.setOnClickListener {
            onClickListener?.onNextClick()
            //dismissAllowingStateLoss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT

            val margin = (16 * resources.displayMetrics.density).toInt()
            params.x = 0

            decorView.setPadding(margin, 0, margin, 0)
            attributes = params
        }

        dialog?.setCanceledOnTouchOutside(false)
    }

    fun showByTag(fragmentManager: FragmentManager, tag: String, listener: OnClickListener) {
        onClickListener = listener
        show(fragmentManager, tag)
    }
}