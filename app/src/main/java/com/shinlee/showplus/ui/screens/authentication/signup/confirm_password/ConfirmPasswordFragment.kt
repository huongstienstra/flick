package com.shinlee.showplus.ui.screens.authentication.signup.confirm_password

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
import com.shinlee.showplus.ARG_SHOW_CONFIRM_PASSWORD
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConfirmPasswordFragment : Fragment() {

    private val viewModel: LoginViewModel by sharedViewModel<LoginViewModel>()
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
        val showConfirmPassword = arguments?.getBoolean(ARG_SHOW_CONFIRM_PASSWORD, true) ?: true

        return inflater.inflate(R.layout.fragment_second_step_signup, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    ConfirmPasswordScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel = viewModel,
                        isShowConfirmPassword = showConfirmPassword,
                        onBackClick = {
                            viewModel.resetPasswordError()
                            navigator?.navigateUp()
                        },
                        onNextStep = {
                            navigator?.navigateToInputNickName()
                        },
                        onLoginSuccess = {
                            navigator?.navigateToMain()
                        }
                    )
                }
            }
        }
    }

}