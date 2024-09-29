package com.shinlee.showplus.ui.screens.authentication.login.v2

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.login.LoginScreen

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModelV2 by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    LoginScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel = viewModel,
                        onBackClick = {
                        },
                        onSignUp = {

                        },
                        onLogin = {

                        }
                    )
                }
            }
        }
    }
}