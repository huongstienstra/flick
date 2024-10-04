package com.shinlee.showplus.ui.screens.authentication.signup.firststep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.CustomInputField
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.TopBar
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel

@Composable
fun FirstStepSignupScreen(
    modifier: Modifier,
    viewModel: SignupViewModel,
    onBackClick: () -> Unit,
    onNext: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isCheckEmailExist == false){
        onNext()
    }else viewModel.validateEmail(uiState.email)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            titleRes = R.string.sign_up_space,
            navigateUp = {
                onBackClick()
            }
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 200.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            label = stringResource(R.string.enter_email_address),
            onValueChange = {
                viewModel.updateEmail(it)
            },
            placeholder = "email@email.com",
            error = uiState.emailError,
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.next),
            enable = uiState.emailError == null,
            onClick = {
                viewModel.checkEmailExit()
            }
        )
    }
}


@Preview
@Composable
fun FirstStepSignupScreen() {
}