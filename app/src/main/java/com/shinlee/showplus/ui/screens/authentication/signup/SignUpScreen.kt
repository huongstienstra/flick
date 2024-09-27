package com.shinlee.showplus.ui.screens.authentication.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
import com.nativemobilebits.loginflow.navigation.Screen
import com.nativemobilebits.loginflow.navigation.SystemBackButtonHandler
import com.shinlee.common.composable.ButtonIconComponent
import com.shinlee.common.composable.DividerTextComponent
import com.shinlee.common.composable.HeadingTextComponent
import com.shinlee.common.composable.NormalTextFieldComponent
import com.shinlee.common.composable.NormalTextComponent

@Composable
fun SignUpScreen(signupViewModel: SignupViewModel = viewModel()) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                NormalTextComponent("khanh")
                HeadingTextComponent(value = "khanh")
                Spacer(modifier = Modifier.height(20.dp))

                NormalTextFieldComponent(
                    placeholder = "khanh",
                    onTextChanged = {
//                        signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()
                Spacer(modifier = Modifier.height(20.dp))

                ButtonIconComponent(
                    value = "Next",
                    onButtonClicked = {
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))

                ButtonIconComponent(
                    value = "Next",
                    onButtonClicked = {
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))

                ButtonIconComponent(
                    value = "Next",
                    onButtonClicked = {
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value
                )

            }

        }

        if(signupViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
        SystemBackButtonHandler {
            PostOfficeAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    SignUpScreen()
}