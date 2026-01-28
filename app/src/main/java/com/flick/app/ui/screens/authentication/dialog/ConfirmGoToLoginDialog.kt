package com.flick.app.ui.screens.authentication.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.flick.app.R

class ConfirmGoToLoginDialog(
    val onLoginClick : () -> Unit,
    val onCancelClick: () -> Unit,
    val title: String = "",
    val content: String = ""
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate your layout here
        val dialog = super.onCreateDialog(savedInstanceState)
        // Set up dialog properties, if needed
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(), // 80% of screen width
            WindowManager.LayoutParams.WRAP_CONTENT // Wrap content for height
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_confirm_go_login, container, false)

        val titleView: TextView = view.findViewById(R.id.tv_title)
        val messageView: TextView = view.findViewById(R.id.tv_content)
        val btnLogin: Button = view.findViewById(R.id.btnLogin)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)

        titleView.text = this.title
        messageView.text = this.content

        btnCancel.setOnClickListener {
            onCancelClick()
            dismiss()
        }
        btnLogin.setOnClickListener {
            onLoginClick()
            dismiss()
        }

        return view
    }
}