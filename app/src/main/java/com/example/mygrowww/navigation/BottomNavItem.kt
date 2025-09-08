package com.example.mygrowww.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Explore : BottomNavItem(
        route = Screen.Explore.route,
        title = "Explore",
        icon = Icons.Default.Home
    )

    object WatchList : BottomNavItem(
        route = Screen.WatchList.route,
        title = "Watchlist",
        icon = Icons.Default.Favorite
    )
}