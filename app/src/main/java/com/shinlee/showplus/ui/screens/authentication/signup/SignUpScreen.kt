package com.shinlee.showplus.ui.screens.authentication.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.OutlinedCustomButton
import com.shinlee.common.composable.SocialButtonHorizontal
import com.shinlee.common.composable.TextDivider
import com.shinlee.common.composable.TopBar
import com.shinlee.common.theme.AppSpace

@Composable
fun SignUpScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onSignupByEmailClick: () -> Unit,
    onGotoLoginClick: () -> Unit) {
    Column(
        modifier = modifier,
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpace.space56dp)
                .background(Color.White),
            navigateUp = {
                onBackClick()
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_showplus),
                contentDescription = "Description of the image"
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(R.string.sign_up_space),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 49.dp),
            text = stringResource(R.string.continue_with_email),
            onClick = {
                onSignupByEmailClick()
            }
        )

        TextDivider(
            text = stringResource(R.string.or),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 43.dp)
        )

        SocialButtonHorizontal(
            icon = R.drawable.ic_google_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 39.dp),
            value = stringResource(R.string.continue_with_google),
        ) {

        }
        SocialButtonHorizontal(
            icon = R.drawable.ic_kakao_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
            value = stringResource(R.string.continue_with_kakao_talk),
        ) {

        }
        SocialButtonHorizontal(
            icon = R.drawable.ic_naver_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
            value = stringResource(R.string.continue_with_naver),
        ) {

        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedCustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = stringResource(R.string.login_with_an_existing_account),
            onClick = {
                onGotoLoginClick()
            }
        )
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
}