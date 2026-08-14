package com.example.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val index: Int
) {
    object Home : Screen(
        route = "home",
        title = "Explore",
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        index = 0
    )

    object Workspace : Screen(
        route = "workspace",
        title = "Workspace",
        selectedIcon = Icons.Filled.GridView,
        unselectedIcon = Icons.Outlined.GridView,
        index = 1
    )

    object Activity : Screen(
        route = "activity",
        title = "Activity",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        index = 2
    )

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        index = 3
    )

    companion object {
        val items: List<Screen>
            get() = listOf(Home, Workspace, Activity, Settings)
    }
}
