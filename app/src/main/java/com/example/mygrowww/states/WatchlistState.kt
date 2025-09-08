package com.example.mygrowww.states

import com.example.mygrowww.database.WatchList

data class WatchlistState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val watchlists: List<WatchList> = emptyList()
)