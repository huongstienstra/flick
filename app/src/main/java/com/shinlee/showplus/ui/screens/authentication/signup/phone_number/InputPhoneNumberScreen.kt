package com.shinlee.showplus.ui.screens.authentication.signup.phone_number

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.shinlee.common.R
import com.shinlee.common.composable.buttons.GradientButton
import com.shinlee.common.composable.TopBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.common.composable.CountryCodeSelector
import com.shinlee.common.theme.AppSpace
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun InputPhoneNumberScreen(
    modifier: Modifier,
    navigateUp: () -> Unit,
    onNext: () -> Unit,
    onOpenCountryCode: () -> Unit,
    viewModel: PhoneNumberViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            titleRes = R.string.signup,
            navigateUp = navigateUp
        )

        Spacer(modifier = Modifier.padding(top = 60.dp))

        Text(
            text = stringResource(R.string.label_enter_phone_number),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = AppSpace.space16dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppSpace.space12dp,
                    start = AppSpace.space16dp,
                    end = AppSpace.space16dp
                )
        ) {
            CountryCodeSelector(
                countryCode = uiState.selectedCountryCode,
                onSelectorClick = {
                    onOpenCountryCode()
                },
                modifier = Modifier
                    .padding(end = AppSpace.space8dp)
                    .wrapContentSize()
                    .height(44.dp)
                    .background(Color.White)
            )

            PhoneInput(
                value = uiState.phoneNumber ?: "",
                onValueChange = {
                    viewModel.updatePhoneNumber(it)
                },
                modifier = Modifier.fillMaxWidth(),
                focusRequester = focusRequester,
                errorMessage = uiState.phoneNumberError,
            )

        }

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpace.space16dp),
            text = stringResource(R.string.btn_next),
            enable = uiState.phoneNumberError == null,
            onClick = {
                focusManager.clearFocus()
                onNext()
            }
        )
    }
}

@Composable
fun PhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    errorMessage: String? = null,
) {
    Column(modifier = modifier) {
        Box(
            // color = if (errorMessage != null) Color.Red else Color.Gray.copy(alpha = 0.5f)
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = if (errorMessage != null) Color.Red else Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color.White)
                .padding(horizontal = AppSpace.space16dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = VisualTransformation.None,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.placeholder_phone_number),
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester)
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
