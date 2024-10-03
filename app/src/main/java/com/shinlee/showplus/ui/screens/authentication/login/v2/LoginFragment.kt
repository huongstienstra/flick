package com.shinlee.showplus.ui.screens.authentication.login.v2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.shinlee.showplus.MainActivity
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.shinlee.showplus.ui.screens.authentication.login.LoginScreen

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModelV2 by viewModel()
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
                            navigator?.onBackPress()
                        },
                        onSignUp = {
                            navigator?.navigateSignUp()
                        },
                        onLoginSuccess = { isPhoneValidate ->
                            if (isPhoneValidate){
                                navigator?.navigateToMain()
                            }else{
                                navigator?.navigateToThirdStepSignup(viewModel.token.value)
                            }
                        }
                    )
                }
            }
        }
    }

}