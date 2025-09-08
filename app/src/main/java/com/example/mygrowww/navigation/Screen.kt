package com.example.mygrowww.navigation



sealed class Screen(val route: String, val title: String, val icon: Int? = null) {
    object Explore : Screen("explore", "Explore", android.R.drawable.ic_menu_search)
    object WatchList : Screen("watchlist", "Watchlist", android.R.drawable.ic_menu_view)

    object ProductDetails : Screen("product_details/{symbol}", "Product Details") {
        fun createRoute(symbol: String) = "product_details/$symbol"
    }

    object WatchListDetailsScreen : Screen("watchlist_details/{watchlistId}", "Watchlist Details") {
        fun createRoute(watchlistId: Long) = "watchlist_details/$watchlistId"
    }

    object ViewAll : Screen("view_all/{section}", "View All") {
        fun createRoute(section: String) = "view_all/$section"
    }

    object AddToWatchlist : Screen("add_to_watchlist/{symbol}", "Add to Watchlist") {
        fun createRoute(symbol: String) = "add_to_watchlist/$symbol"
    }

    companion object {
        val bottomNavItems = listOf(Explore, WatchList)
    }
}
