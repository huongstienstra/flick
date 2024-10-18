package com.shinlee.showplus.ui.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.LoginState
import com.shinlee.showplus.ui.MainViewModel
import com.shinlee.showplus.ui.screens.authentication.AuthenticationActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.shinlee.showplus.extension.requestLoginDialog


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

        val composeView = view.findViewById<ComposeView>(R.id.compose_view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.loginState.collect { loginState ->
                    composeView.setContent {
                        when (loginState) {
                            LoginState.LoggedIn -> {
                                ProfileScreen(
                                    viewModel = mainViewModel,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                )
                            }

                            LoginState.Unknown -> {
                                // Show loading or placeholder UI
                                // LoadingScreen()
                            }

                            LoginState.IncompleteProfile -> {
                                // Show incomplete profile UI
                                this@ProfileFragment.showLoginDialog()
                            }

                            else -> {
                                // Show login UI
                                // LoginScreen(onLoginClick = { showLoginDialog() })
                            }
                        }
                    }
                }
            }
        }

    }

    private fun showLoginDialog() {
        this@ProfileFragment.requestLoginDialog(
            onNext = {
                startActivity(Intent(context, AuthenticationActivity::class.java))
            },
            onCancel = {
            }

        )
    }

}