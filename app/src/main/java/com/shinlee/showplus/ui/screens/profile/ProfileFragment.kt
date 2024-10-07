package com.shinlee.showplus.ui.screens.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shinlee.common.dialogs.ErrorDialog
import com.shinlee.common.dialogs.OnClickListener
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.LoginState
import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.authentication.AuthenticationActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.shinlee.common.R as R_common


class ProfileFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.checkLoginStatus()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.loginState.collect { loginState ->
                    Log.e("login","view: $loginState")
                    when (loginState) {
                        LoginState.LoggedIn -> {
                            // User is logged in, show profile content
                        }

                        LoginState.Unknown -> {

                        }
                        LoginState.IncompleteProfile-> showLoginDialog()
                        else -> showLoginDialog()
                    }
                }
            }
        }

    }

    private fun showLoginDialog() {
        ErrorDialog.newInstance(
            titleRes = R_common.string.dialog_request_login_title,
            messageRes = R_common.string.dialog_request_login_desc,
            positiveButtonTextRes = R_common.string.dialog_request_login_title_positive,
            negativeButtonTextRes = R_common.string.dialog_request_login_title_negative
        ).showByTag(fragmentManager = childFragmentManager, "", object : OnClickListener {
            override fun onNextClick() {
                startActivity(Intent(context, AuthenticationActivity::class.java))
            }

            override fun onCancelClick() {
                // Handle cancel click
            }
        })
    }

}