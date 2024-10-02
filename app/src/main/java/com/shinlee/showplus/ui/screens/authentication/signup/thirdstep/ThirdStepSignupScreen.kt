package com.shinlee.showplus.ui.screens.authentication.signup.thirdstep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.CustomInputField
import com.shinlee.common.composable.GradientButton
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel

@Composable
fun ThirdStepSignupScreen(
    modifier: Modifier,
    viewModel: SignupViewModel,
    onBackClick: () -> Unit,
    onSignUpSuccess : () -> Unit) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (viewModel.uiState.value.isSignUpSuccess){
        onSignUpSuccess()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 200.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
                label = "Nick name",
                onValueChange = {
                    viewModel.updateEmail(it)
                },
                placeholder = "Enter your nick name",
            )

            GradientButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp),
                text = stringResource(R.string.next),
                enable = uiState.emailError == null,
                onClick = {
                }
            )
        }

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 24.dp, top = 24.dp),
            text = stringResource(R.string.next),
            enable = uiState.passwordError == null && uiState.confirmPasswordError == null,
            onClick = {
                viewModel.registerFirstStep()
            }
        )
    }
}