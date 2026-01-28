import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flick.common.R
import com.flick.common.composable.TopBar
import com.flick.common.theme.AppSpace
import com.flick.common.theme.Gray3Color
import com.flick.app.ui.screens.authentication.signup.LoginViewModel
import com.flick.app.ui.screens.authentication.signup.phone_number.PhoneNumberViewModel

@Composable
fun SixDigitOtpInput(
    modifier: Modifier = Modifier,
    onOtpEntered: (String) -> Unit
) {
    var code by remember { mutableStateOf(List(6) { TextFieldValue() }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 6) {
            BasicTextField(
                value = code[i],
                onValueChange = { newValue ->
                    if (newValue.text.length <= 1 && newValue.text.all { it.isDigit() }) {
                        val newCode = code.toMutableList()
                        newCode[i] = newValue
                        code = newCode
                        if (newValue.text.isNotEmpty() && i < 5) {
                            focusRequesters[i + 1].requestFocus()
                        }
                        if (code.all { it.text.isNotEmpty() }) {
                            onOtpEntered(code.joinToString("") { it.text })
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                    .focusRequester(focusRequesters[i])
                    .onKeyEvent { event ->
                        if (event.key == Key.Backspace && event.type == KeyEventType.KeyUp) {
                            val currentValue = code[i]
                            if (currentValue.text.isEmpty() && i > 0) {
                                focusRequesters[i - 1].requestFocus()
                            } else {
                                val newCode = code.toMutableList()
                                newCode[i] = TextFieldValue(
                                    text = "",
                                    selection = currentValue.selection
                                )
                                code = newCode
                            }
                            true
                        } else {
                            false
                        }
                    },
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
fun OTPScreen(
    modifier: Modifier,
    viewModel: PhoneNumberViewModel,
    onOtpEntered: (String) -> Unit,
    navigateUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNextStep: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.completedLogin) {
        if (uiState.completedLogin) {
            if (viewModel.isLoggedIn()) {
                onLoginSuccess()
            } else {
                onNextStep()
            }
        }
    }

    val textSentOTP =
        String.format(stringResource(R.string.text_otp_sent_to_phone), viewModel.getPhoneNumber())

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            titleRes = null,
            navigateUp = navigateUp,
        )

        Spacer(modifier = Modifier.padding(top = 60.dp))

        Text(
            text = stringResource(R.string.label_enter_otp),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = AppSpace.space16dp)
        )


        Text(
            text = textSentOTP,
            color = Gray3Color,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(
                    start = AppSpace.space16dp,
                    end = AppSpace.space16dp,
                    top = AppSpace.space32dp
                )
        )

        SixDigitOtpInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onOtpEntered = {
                onOtpEntered(it)
            }
        )

    }
}
