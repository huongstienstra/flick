package com.shinlee.showplus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shinlee.showplus.HomeScreen
import com.shinlee.showplus.ui.screens.contest.ContestScreen
import com.shinlee.showplus.ui.screens.profile.ProfileScreen
import com.shinlee.showplus.ui.screens.search.SearchScreen
import com.shinlee.showplus.ui.screens.show.ShowScreen
import com.shinlee.showplus.ui.screens.upload.UploadScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SHOW,
        modifier = modifier
    ) {
        composable(Routes.SHOW) {
            ShowScreen()
        }
        composable(Routes.CONTEST) {
            ContestScreen()
        }
        composable(Routes.UPLOAD) {
            UploadScreen()
        }
        composable(Routes.SEARCH) {
            SearchScreen()
        }
        composable(Routes.PROFILE) {
            ProfileScreen()
        }
    }
}

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    composable(route = "home") {
        HomeScreen(navController)
    }
}