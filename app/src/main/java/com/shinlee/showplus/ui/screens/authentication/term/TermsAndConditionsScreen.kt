package com.shinlee.showplus.ui.screens.authentication.term

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
import com.nativemobilebits.loginflow.navigation.Screen
import com.nativemobilebits.loginflow.navigation.SystemBackButtonHandler
import com.shinlee.common.composable.ButtonComponent
import com.shinlee.common.composable.CheckboxComponent
import com.shinlee.common.composable.NormalTextComponent
import com.shinlee.showplus.ui.screens.authentication.login.LoginUIEvent
import com.shinlee.showplus.ui.screens.authentication.signup.SignUpScreen

@Composable
fun TermsAndConditionsScreen(termViewModel: TermViewModel = viewModel()) {
    val checkedState = remember {
        mutableStateOf(false)
    }
    Surface(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 0.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                NormalTextComponent("Agree to Term and Conditions")

                CheckboxComponent(value = "Select All ", "",
                    onTextSelected = {},
                    onCheckedChange = {
                        checkedState.value = true
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                CheckboxComponent(value = "Term of service ","[Require]",
                    onTextSelected = {},
                    onCheckedChange = {
                        checkedState.value
                    }
                )
                CheckboxComponent(value = "Privacy Policy ", "[Require]",
                    onTextSelected = {},
                    onCheckedChange = {
                        checkedState.value
                    }
                )
                CheckboxComponent(value = "Marketing Communication ", "[Require]",
                    onTextSelected = {},
                    onCheckedChange = {}
                )

                Spacer(modifier = Modifier.height(20.dp))

                ButtonComponent(
                    value = "Next",
                    onButtonClicked = {
                        PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
                    },
                    isEnabled = true                )
            }
        }
        SystemBackButtonHandler {
            PostOfficeAppRouter.navigateTo(Screen.LoginScreen)
        }
    }
}

@Preview
@Composable
fun TermsAndConditionsScreenPreview() {
    TermsAndConditionsScreen()
}