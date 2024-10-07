package com.shinlee.showplus.ui.screens.authentication.signup.confirm_password

import android.util.Log
import android.view.textservice.TextInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.input.CustomInputField
import com.shinlee.common.composable.buttons.GradientButton
import com.shinlee.common.composable.TopBar
import com.shinlee.common.theme.Gray3Color
import com.shinlee.common.theme.TextInfo
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel

@Composable
fun ConfirmPasswordScreen(
    modifier: Modifier,
    viewModel: LoginViewModel,
    isShowConfirmPassword: Boolean,
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNextStep: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val focusManager: FocusManager = LocalFocusManager.current

    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.completedLogin) {
        if (uiState.completedLogin) {
            if (viewModel.isLoggedIn()) {
                onLoginSuccess()
            } else {
                onNextStep()
            }
        }
    }

    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
    }

//    LaunchedEffect(uiState.errorMessage) {
//        if (!uiState.errorMessage.isNullOrEmpty()) {
//            showErrorDialog = true
//        }
//    }

    Column(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = {
                    showErrorDialog = false
                    viewModel.resetErrorMessage()
                },
                title = { Text("Error") },
                text = { Text(uiState.errorMessage ?: "") },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        onClick = {
                            viewModel.resetErrorMessage()
                            showErrorDialog = false
                        }) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }

        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            titleRes = null,
            navigateUp = {
                onBackClick()
                focusManager.clearFocus()
            }
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .focusRequester(passwordFocusRequester),
            label = stringResource(R.string.label_enter_password),
            onValueChange = {
                viewModel.updatePassword(it)
            },
            placeholder = stringResource(R.string.placeholder_password),
            error = uiState.passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ).copy(
                imeAction = if (isShowConfirmPassword) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if (isShowConfirmPassword) {
                        confirmPasswordFocusRequester.requestFocus()
                    }
                },
                onDone = {
                    if (!isShowConfirmPassword) {
                        focusManager.clearFocus()
                    }
                }
            ),

            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(id = R.drawable.ic_password_visible) else painterResource(
                            id = R.drawable.ic_password_invisible
                        ),
                        tint = Gray3Color,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        if (!isShowConfirmPassword) {
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(text = "Forgot your password?", color = TextInfo, fontWeight = FontWeight.Bold)
            }

        }

        if (isShowConfirmPassword) {
            CustomInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                    .focusRequester(confirmPasswordFocusRequester),
                label = stringResource(R.string.label_re_enter_password),
                onValueChange = {
                    viewModel.updateConfirmPassword(it)
                },
                placeholder = stringResource(R.string.placeholder_password),
                error = uiState.confirmPasswordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = if (confirmPasswordVisible) painterResource(id = R.drawable.ic_password_visible) else painterResource(
                                id = R.drawable.ic_password_invisible
                            ),
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            tint = Gray3Color
                        )
                    }
                }
            )
        }
        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.btn_next),
            enable = viewModel.isPasswordValid(isShowConfirmPassword),
            onClick = {
                viewModel.onLoginEmail()
                focusManager.clearFocus()
            }
        )
    }
}