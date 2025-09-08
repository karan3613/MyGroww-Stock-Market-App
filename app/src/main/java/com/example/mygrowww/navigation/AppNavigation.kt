package com.example.mygrowww.navigation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mygrowww.screens.ExploreScreen
import com.example.mygrowww.screens.ProductScreen
import com.example.mygrowww.screens.ViewAllScreen
import com.example.mygrowww.screens.WatchListScreen
import com.example.mygrowww.screens.WatchlistDetailScreen

//MAIN NAVIGATION FOR THE APP WITH THE BOTTOM NAVIGATION BAR
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        containerColor = Color.Black,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Explore.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // ---- Bottom navigation destinations ----
            composable(Screen.Explore.route) {
                ExploreScreen(
                    onNavigateToViewAll = { section ->
                        navController.navigate(Screen.ViewAll.createRoute(section))
                    } ,
                    onNavigateToProductDetails = {symbol ->
                        navController.navigate(Screen.ProductDetails.createRoute(symbol))
                    }
                )
            }

            composable(Screen.WatchList.route) {
                WatchListScreen(
                    onNavigateToWatchListDetails = { watchlistId ->
                        navController.navigate(Screen.WatchListDetailsScreen.createRoute(watchlistId))
                    }
                )
            }

            // ---- Full screen destinations ----
            composable(
                route = Screen.ViewAll.route,
                arguments = listOf(navArgument("section") { type = NavType.StringType })
            ) { backStackEntry ->
                val section = backStackEntry.arguments?.getString("section") ?: ""
                ViewAllScreen(
                    section = section,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProductDetails = { symbol ->
                        navController.navigate(Screen.ProductDetails.createRoute(symbol))
                    }
                )
            }

            composable(
                route = Screen.ProductDetails.route,
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
                ProductScreen(
                    symbol = symbol,
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            composable(
                route = Screen.WatchListDetailsScreen.route,
                arguments = listOf(navArgument("watchlistId") { type = NavType.LongType })
            ) { backStackEntry ->
                val watchlistId = backStackEntry.arguments?.getLong("watchlistId") ?: 0
                WatchlistDetailScreen(
                    watchlistId = watchlistId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProductDetails = { symbol ->
                        navController.navigate(Screen.ProductDetails.createRoute(symbol))
                    }
                )
            }


        }
    }
}

