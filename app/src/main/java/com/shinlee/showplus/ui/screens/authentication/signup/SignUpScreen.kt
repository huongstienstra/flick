package com.shinlee.showplus.ui.screens.authentication.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinlee.common.R
import com.shinlee.common.composable.GradientButton
import com.shinlee.common.composable.SocialButtonHorizontal
import com.shinlee.common.composable.TextDivider

@Composable
fun SignUpScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onSignupByEmailClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.height(56.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.continue_with_email),
            onClick = {
                onSignupByEmailClick()
            }
        )

        TextDivider(
            text = stringResource(R.string.or),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
        )

        SocialButtonHorizontal(
            icon = R.drawable.ic_google_register,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
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
    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
}