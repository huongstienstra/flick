package com.flick.common.composable.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flick.common.R
import com.flick.common.theme.RedColor

@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var value by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        label?.let {
            Text(
                text = label,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 16.sp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color.White)
        ) {
            BasicTextField(
                value = value,
                onValueChange = {
                    value = it
                    onValueChange(value)
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                        if (trailingIcon != null) {
                            trailingIcon()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = RedColor,
                // style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun InputEmailPreview() {
    CustomInputField(
        modifier = Modifier.fillMaxWidth(),
        label = "Email address",
        onValueChange = { },
        placeholder = "email@email.com",
        error = "is not valid",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(imeAction = ImeAction.Done)
    )
}

@Preview
@Composable
fun InputPasswordPreview() {
    var passwordVisible by remember { mutableStateOf(false) }

    val iconImage = if (passwordVisible) {
        painterResource(id = R.drawable.ic_password_visible)
    } else {
        painterResource(id = R.drawable.ic_password_invisible)
    }
    CustomInputField(
        modifier = Modifier.fillMaxWidth(),
        label = "Password",
        onValueChange = {
            //viewModel.onPasswordChanged(it)
        },
        placeholder = "8-16 character with letters & numbers",
        error = "password should be at least 8 characters",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = iconImage,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Color.Gray
                )
            }
        }
    )

}

