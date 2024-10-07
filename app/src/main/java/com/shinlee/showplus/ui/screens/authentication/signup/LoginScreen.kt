package com.shinlee.showplus.ui.screens.authentication.signup

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.R
import com.shinlee.common.composable.buttons.GradientButton
import com.shinlee.common.composable.buttons.SocialButtonHorizontal
import com.shinlee.common.composable.TextDivider
import com.shinlee.common.composable.TopBar
import com.shinlee.common.composable.input.CustomInputField
import com.shinlee.common.theme.AppSpace
import com.shinlee.common.theme.Gray3Color

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navigateUp: () -> Unit,
    openInputPassword: (isShowConfirmPassword: Boolean) -> Unit,
    onSigUpWithPhoneNumber: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current


    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            Log.e("login", "LaunchedEffect $event")
            when (event) {
                AuthenticationFlowType.SIGN_UP-> {
                    openInputPassword(true)
                }
                AuthenticationFlowType.LOGIN -> {
                    openInputPassword(false)
                }

                else -> {}
            }
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpace.space56dp)
                .background(Color.White),
            navigateUp = {
                focusManager.clearFocus()
                navigateUp()
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_showplus),
                contentDescription = "Description of the image"
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(R.string.signup),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        CustomInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done),
            onValueChange = {
                viewModel.updateEmail(it)
            },
            placeholder = stringResource(R.string.placeholder_email),
            error = uiState.emailError,
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp),
            text = stringResource(R.string.button_login_with_email),
            onClick = {
                viewModel.identifyEmail()
            },
            enable = uiState.emailError == null
        )

        TextDivider(
            text = stringResource(R.string.text_or),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        SocialButtonHorizontal(
            icon = R.drawable.ic_phone_number,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            value = stringResource(R.string.button_login_with_phone_number),
        ) {
            onSigUpWithPhoneNumber()

        }

        SocialButtonHorizontal(
            icon = R.drawable.ic_google_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            value = stringResource(R.string.button_login_with_google),
        ) {

        }
        SocialButtonHorizontal(
            icon = R.drawable.ic_kakao_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            value = stringResource(R.string.button_login_with_kakao_talk),
        ) {

        }
        SocialButtonHorizontal(
            icon = R.drawable.ic_naver_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            value = stringResource(R.string.button_login_with_naver),
        ) {

        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            textAlign = TextAlign.Center,
            color = Gray3Color,
            fontSize = 12.sp,
            text = stringResource(R.string.text_agree_to_term_and_condition)
        )
    }
}