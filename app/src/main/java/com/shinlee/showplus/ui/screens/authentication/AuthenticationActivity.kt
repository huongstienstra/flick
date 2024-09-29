package com.shinlee.showplus.ui.screens.authentication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.shinlee.showplus.MarvelViewModel
import com.shinlee.showplus.ui.screens.authentication.login.LoginViewModel
import com.shinlee.showplus.ui.screens.authentication.navigation.PostFlowAuthentication
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity : AppCompatActivity() {
    private val marvelViewModel: MarvelViewModel by viewModel()

    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostFlowAuthentication(marvelViewModel, loginViewModel, this)
        }
    }
}
