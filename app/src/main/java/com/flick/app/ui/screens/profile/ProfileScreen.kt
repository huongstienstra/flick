package com.flick.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flick.app.ui.LoginState
import com.flick.app.ui.MainViewModel

/**
 * Profile screen composable
 */
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.loginState.collectAsState()
    val profileState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (loginState) {
            LoginState.LoggedIn -> {
                LoggedInProfile(
                    userName = profileState.userName,
                    modifier = Modifier.fillMaxSize()
                )
            }
            LoginState.LoggedOut, LoginState.Unknown, LoginState.TokenExpired -> {
                NotLoggedInProfile(
                    onLoginClick = onLoginClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
            LoginState.IncompleteProfile -> {
                IncompleteProfile(
                    onCompleteClick = onLoginClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun LoggedInProfile(
    userName: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Profile avatar placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(60.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName ?: "User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome to Flick!",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun NotLoggedInProfile(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            modifier = Modifier.size(80.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sign in to see your profile",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                text = "Sign In",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun IncompleteProfile(
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Complete your profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please finish setting up your profile to continue",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCompleteClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                text = "Complete Profile",
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
