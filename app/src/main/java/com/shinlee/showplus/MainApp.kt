package com.shinlee.showplus

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.puskal.tiktokcompose.component.BottomBar
import com.shinlee.showplus.navigation.AppNavHost
import com.shinlee.common.theme.ShowplusTheme

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntryAsState?.destination
    val context = LocalContext.current


//    val isShowBottomBar = when (currentDestination?.route) {
//        HOME_SCREEN_ROUTE, INBOX_ROUTE, COMMENT_BOTTOM_SHEET_ROUTE,
//        FRIENDS_ROUTE, AUTHENTICATION_ROUTE, MY_PROFILE_ROUTE, null -> true
//        else -> false
//    }
//    val darkMode = when (currentDestination?.route) {
//        HOME_SCREEN_ROUTE, FORMATTED_COMPLETE_CREATOR_VIDEO_ROUTE, CAMERA_ROUTE, null -> true
//        else -> false
//    }

//    if (currentDestination?.route == "home") {
//        BackHandler {
//            (context as? Activity)?.finish()
//        }
//    }

    ShowplusTheme {

        Scaffold(
            topBar = {
                Text("App Bar")
            },
            bottomBar = {
//                    if (!isShowBottomBar) {
//                        return@Scaffold
//                    }
                BottomBar(navController)
            }
        ) { innerPadding ->

            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}