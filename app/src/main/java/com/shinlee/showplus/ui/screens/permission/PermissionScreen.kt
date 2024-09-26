package com.shinlee.showplus.ui.screens.permission

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.components.ButtonComponent
import com.shinlee.showplus.ui.theme.PinkColor



@Composable
fun PermissionScreen(pPermissionCheckEvent: PermissionCheckEvent, onNextAction: () -> Unit) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enable Permissions", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
            Text(text = "Unlock full app potential with seamless, personalized access",  fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            PermissionCard("Camera","Grant camera access to capture abd share moments instantly", R.drawable.ic_camera)
            PermissionCard("Microphone", "Grant camera access to capture abd share moments instantly", R.drawable.ic_camera)
            PermissionCard("Microphone","Grant camera access to capture abd share moments instantly", R.drawable.ic_camera)
            PermissionCard("Notification", "Grant camera access to capture abd share moments instantly", R.drawable.ic_camera)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "* Device setting - changes can be made by permission in Showplus", fontSize = 15.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "* If permission is denied, the function may not work properly", fontSize = 15.sp, color = Color.Black)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomEnd,
        ) {
            ButtonComponent(
                value = "Next",
                onButtonClicked = {
                    pPermissionCheckEvent.checkPermissionButtonClicked()
                    onNextAction()
                },
                isEnabled = true
            )
        }
    }
}



@Composable
fun PermissionCard(title: String, content: String, linkImage: Int) {
    Row(modifier = Modifier.padding(all = 20.dp)) {
        Image(
            painter = painterResource(linkImage),
            contentDescription = null,
            modifier = Modifier
                .padding(all = 10.dp)
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 15.sp)) {
                        append(title)
                    }
                    withStyle(style = SpanStyle(color = PinkColor, fontSize = 15.sp)) {
                        append(" [Require]")
                    }
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = content,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

