package com.shinlee.showplus.ui.screens.authentication.signup.thirdstep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.CustomInputField
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.TopBar
import com.shinlee.common.composable.verifyBlackButtonComponent
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel

@Composable
fun ThirdStepSignupScreen(
    modifier: Modifier,
    viewModel: SignupViewModel,
    onBackClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onGetOtpClick: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (viewModel.uiState.value.isSignUpSuccess == true) {
        onSignUpSuccess()
    }

    val isNextEnabled = remember(uiState) {
        uiState.nickNameError == null
                && uiState.phoneNumberError == null

    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
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
                .padding(start = 24.dp, end = 24.dp, top = 105.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            label = "Nick name",
            onValueChange = {
                viewModel.updateNickName(it)
            },
            placeholder = "Enter your nick name",
            error = uiState.nickNameError
        )

        Text(
            modifier = Modifier.padding(top = 12.dp, start = 24.dp),
            text = "Verify your identity",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 16.sp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
        ) {
            verifyBlackButtonComponent(
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp)
                    .weight(1f),
                text = "+84",
                onClick = {
                }
            )

            CustomInputField(
                modifier = Modifier
                    .weight(3f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone).copy(
                    imeAction = ImeAction.Done
                ),
                label = "",
                onValueChange = {
                    viewModel.updatePhoneNumber(it)
                },
                placeholder = "Enter your phone",
                error = uiState.phoneNumberError
            )

            verifyBlackButtonComponent(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .weight(1f),
                text = "Send",
                onClick = {
                    onGetOtpClick(uiState.phoneNumber)
                }
            )
        }

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            label = "Enter 6-digit number",
            onValueChange = {
            },
            placeholder = "Enter your code sent to "
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp),
            text = stringResource(R.string.next),
            enable = isNextEnabled,
            onClick = {
            }
        )
    }
}