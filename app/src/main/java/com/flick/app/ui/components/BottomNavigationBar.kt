package com.flick.app.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.flick.app.navigation.BottomNavItems
import com.flick.app.navigation.Screen

/**
 * Bottom navigation bar for the main app screens
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        BottomNavItems.items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(text = screen.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,
                    selectedTextColor = Color.Red,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}
