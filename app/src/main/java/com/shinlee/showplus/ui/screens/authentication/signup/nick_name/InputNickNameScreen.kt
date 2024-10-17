package com.shinlee.showplus.ui.screens.authentication.signup.nick_name

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.TopBar
import com.shinlee.common.composable.buttons.GradientButton
import com.shinlee.common.composable.input.CustomInputField
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel


@Composable
fun InputNickNameScreen(
    modifier: Modifier,
    viewModel: LoginViewModel,
    onBackClick: () -> Unit,
    onNext: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    var showErrorDialog by remember { mutableStateOf(false) }

    val nickNameFocusRequester = remember { FocusRequester() }
    val codeFocusRequester = remember { FocusRequester() }


    LaunchedEffect(uiState.completedNickName) {
        if (uiState.completedNickName) {
            onNext()
        }
    }

    LaunchedEffect(Unit) {
        nickNameFocusRequester.requestFocus()
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

//        if (showErrorDialog) {
//            AlertDialog(
//                onDismissRequest = {
//                    showErrorDialog = false
//                    viewModel.resetErrorMessage()
//                },
//                title = { Text("Error") },
//                text = { Text(uiState.errorMessage ?: "") },
//                confirmButton = {
//                    Button(
//                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
//                        onClick = {
//                            viewModel.resetErrorMessage()
//                            showErrorDialog = false
//                        }) {
//                        Text("OK", color = Color.White)
//                    }
//                }
//            )
//        }

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
                .focusRequester(nickNameFocusRequester),
            label = stringResource(R.string.label_input_nick_name),
            onValueChange = {
                viewModel.updateNickName(it)
            },
            placeholder = stringResource(R.string.place_holder_add_nickname),
            error = uiState.nickNameError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    codeFocusRequester.requestFocus()
                },
            ),
        )

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                .focusRequester(codeFocusRequester),
            label = stringResource(R.string.label_invited_code),
            onValueChange = {
                viewModel.updateInvitedCode(it)
            },
            error = null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )


        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.btn_next),
            enable = true,
            onClick = {
                viewModel.submitNickNameAndInviteCode()
                focusManager.clearFocus()
            }
        )
    }
}