package com.flick.app.ui.screens.authentication.signup.nick_name

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
import com.flick.app.R
import com.flick.app.extension.showExitConfirmationDialog
import com.flick.app.ui.screens.authentication.nav.AuthNavigator
import com.flick.app.ui.screens.authentication.signup.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class InputNickNameFragment : Fragment() {

    private var navigator: AuthNavigator? = null

    private val viewModel: LoginViewModel by sharedViewModel<LoginViewModel>()

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
        return inflater.inflate(R.layout.fragment_term, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    InputNickNameScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel = viewModel,
                        onBackClick = {
                            this@InputNickNameFragment.showExitConfirmationDialog(
                                title = com.flick.common.R.string.dialog_return_complete_nick_name_title,
                                desc = com.flick.common.R.string.dialog_return_complete_nick_name_desc,
                                positiveBtn = com.flick.common.R.string.dialog_return_complete_nick_name_positive,
                                negativeBtn = com.flick.common.R.string.dialog_return_complete_nick_name_negative,
                                onNext = {
                                    navigator?.navigateToMain()
                                },
                                onCancel = {

                                }
                            )
                        },
                        onNext = {
                            navigator?.navigateToMain()
                        }
                    )
                }
            }
        }
    }
}