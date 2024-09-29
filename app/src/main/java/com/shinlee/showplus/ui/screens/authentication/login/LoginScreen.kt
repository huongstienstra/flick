package com.shinlee.showplus.ui.screens.authentication.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.shinlee.common.composable.EasyLoginDivider
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.OutlinedCustomButton
import com.shinlee.common.composable.SocialLoginButton
import com.shinlee.showplus.ui.screens.authentication.login.v2.LoginViewModelV2

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModelV2,
    onBackClick: () -> Unit,
    onSignUp: () -> Unit,
    onLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text = "Show+ Log in",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            label = "Email Address",
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
            label = "Password",
            onValueChange = {
                viewModel.updatePassword(it)
            },
            placeholder = "8-16 character with letters & numbers",
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
            text = "Login with Email/ID",
            onClick = {
                onLogin()
            }
        )

        Text(
            text = "Forgot your ID or password?",
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 36.dp)
        )


        EasyLoginDivider(
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
                R.drawable.google_social_button,
                "Google",
                onClick = {})
            SocialLoginButton(
                R.drawable.google_social_button,
                "Apple",
                onClick = {})
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedCustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = "SignUp",
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