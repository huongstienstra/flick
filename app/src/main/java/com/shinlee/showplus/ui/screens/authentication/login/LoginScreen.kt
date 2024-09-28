package com.shinlee.showplus.ui.screens.authentication.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
import com.nativemobilebits.loginflow.navigation.Screen
import com.shinlee.common.composable.ButtonComponent
import com.shinlee.common.composable.ClickableLoginTextComponent
import com.shinlee.common.composable.DividerTextComponent
import com.shinlee.common.composable.ErrorTextComponent
import com.shinlee.common.composable.HeadingTextComponent
import com.shinlee.common.composable.NormalTextFieldComponent
import com.shinlee.common.composable.NormalTextComponent
import com.shinlee.common.composable.PasswordTextFieldComponent
import com.shinlee.common.composable.UnderLinedTextComponent

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 0.dp )
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                HeadingTextComponent(value = "Login to Show+")
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    Modifier.align(Alignment.Start)
                ) {
                    NormalTextComponent("Email Address")
                    NormalTextFieldComponent(
                        "email@email.com",
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError
                    )

                    ErrorTextComponent("errorfvvx")


                    Spacer(modifier = Modifier.height(20.dp))

                    NormalTextComponent("Password")
                    PasswordTextFieldComponent(
                        placeholder = "8-16 character with letters & numbers.",
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError
                    )
                    ErrorTextComponent("errorfvvx")
                }


                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = "Login with Email/iD",
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(40.dp))
                UnderLinedTextComponent(value = "Forgot your ID or Password?")
                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                Row (){
                    IconButton(onClick = { /* Handle button click */ }) {
                        Image(
                            painter = painterResource(id = com.shinlee.common.R.drawable.ic_google_login),
                            contentDescription = "Image Button",
                            modifier = Modifier.size(100.dp) // Set size as needed
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    IconButton(onClick = { /* Handle button click */ }) {
                        Image(
                            painter = painterResource(id = com.shinlee.common.R.drawable.ic_facebook_login),
                            contentDescription = "Image Button",
                            modifier = Modifier.size(100.dp) // Set size as needed
                        )
                    }
                }
            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomEnd,
        ) {
            ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                PostOfficeAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
            })
        }

//        SystemBackButtonHandler {
//            PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
//        }
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}