package com.puskal.tiktokcompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shinlee.showplus.navigation.BottomBarDestination


@Composable
fun BottomBar(navController: NavController) {

    BottomNavigation(
        modifier = Modifier
            .systemBarsPadding(),
        backgroundColor = Color.White,
        elevation = 16.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomBarDestination.entries.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(screen.icon),
                        contentDescription = null, tint = if (!isSelected) Color.Gray else Color.Red
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.title),
                        color = if (!isSelected) Color.Gray else Color.Red
                    )
                }
            )
        }
    }
}
