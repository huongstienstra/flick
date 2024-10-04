package com.shinlee.showplus.ui.screens.authentication.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModel()
    private var navigator: AuthNavigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthNavigator) {
            navigator = context
        } else {
            throw ClassCastException("$context must implement FragmentNavigation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    SignUpScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        onSignupByEmailClick = {
                            navigator?.navigateTerm()
                        },
                        onBackClick = {
                            navigator?.onBackPress()
                        },

                        onGotoLoginClick = {
                            navigator?.navigateToLogin()
                        }
                    )
                }
            }
        }
    }
}