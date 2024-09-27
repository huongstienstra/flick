package com.shinlee.showplus.ui.screens.authentication.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter.currentScreen
import com.nativemobilebits.loginflow.navigation.Screen
import com.shinlee.showplus.MainActivity
import com.shinlee.showplus.MarvelViewModel
import com.shinlee.showplus.ui.screens.authentication.login.LoginScreen
import com.shinlee.showplus.ui.screens.authentication.login.LoginViewModel
import com.shinlee.showplus.ui.screens.authentication.signup.SignUpScreen
import com.shinlee.showplus.ui.screens.authentication.term.TermsAndConditionsScreen

@Composable
fun PostFlowAuthentication(marvelViewModel: MarvelViewModel = viewModel(),
                           loginViewModel: LoginViewModel, context: Context
) {

//    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

//        if (homeViewModel.isUserLoggedIn.value == true) {
//            PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
//        }

        Crossfade(targetState = PostOfficeAppRouter.currentScreen) { currentState ->
            when (currentState.value) {
                is Screen.LoginScreen -> {
                    LoginScreen(loginViewModel)
                }

                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }

                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }

                is Screen.GoToMainScreen -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    currentScreen.value = Screen.LoginScreen
                }

                Screen.ForgotPasswordScreen -> {

                }
            }
        }

    }
}