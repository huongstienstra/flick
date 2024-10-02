package com.shinlee.showplus.ui.screens.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.CustomInputField
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.OutlinedCustomButton
import com.shinlee.common.composable.SocialLoginButton
import com.shinlee.common.composable.TextDivider
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginViewModelV2

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModelV2,
    onBackClick: () -> Unit,
    onSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }

    if (uiState.isLoggedIn) {
        onLoginSuccess()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(56.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_logo_showplus),
                contentDescription = "Description of the image"
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(R.string.log_in),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            label = stringResource(R.string.email_address),
            onValueChange = {
                viewModel.updateEmail(it)
            },
            placeholder = "email@email.com",
            error = uiState.emailError,
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
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

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.login_with_email),
            enable = uiState.emailError == null && uiState.passwordError == null,
            onClick = {
                viewModel.loginByEmail()
            }
        )

        Text(
            text = stringResource(R.string.forgot_your_id_or_password),
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 36.dp)
        )


        TextDivider(
            text = stringResource(R.string.easy_login_with),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            SocialLoginButton(
                R.drawable.ic_google_login,
                "Google",
                onClick = {})
            SocialLoginButton(
                R.drawable.ic_line_login,
                "Apple",
                onClick = {})

            SocialLoginButton(
                R.drawable.ic_kakao_login,
                "Apple",
                onClick = {})
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedCustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = stringResource(R.string.sign_up),
            onClick = {
                onSignUp()
            }
        )
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
//    LoginScreen(
//        modifier = Modifier.background(Color.White),
//        onBackClick = {},
//        onLogin = {},
//        onSignUp = {})
}