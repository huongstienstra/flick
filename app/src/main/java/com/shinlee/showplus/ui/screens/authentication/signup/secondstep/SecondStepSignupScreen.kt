package com.shinlee.showplus.ui.screens.authentication.signup.secondstep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
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
import com.shinlee.common.composable.TopBar
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel


@Composable
fun SecondStepSignupScreen(
    modifier: Modifier,
    viewModel: SignupViewModel,
    onBackClick: () -> Unit,
    onSignUpSuccess : () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    if (viewModel.uiState.value.isSignUpSuccess){
        onSignUpSuccess()
    }

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
                .padding(top = 105.dp, start = 25.dp, end = 24.dp),
            label = stringResource(R.string.password),
            onValueChange = {
                viewModel.updatePassword(it)
            },
            placeholder = stringResource(R.string.character_with_letters_numbers),
            error = uiState.passwordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(imeAction = ImeAction.Done),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(id = R.drawable.ic_password_visible) else painterResource(
                            id = R.drawable.ic_password_invisible
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 25.dp, end = 24.dp),
            label = stringResource(R.string.re_enter_password),
            onValueChange = {
                viewModel.updateConfirmPassword(it)
            },
            placeholder = stringResource(R.string.character_with_letters_numbers),
            error = uiState.confirmPasswordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(imeAction = ImeAction.Done),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = if (confirmPasswordVisible) painterResource(id = R.drawable.ic_password_visible) else painterResource(
                            id = R.drawable.ic_password_invisible
                        ),
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

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