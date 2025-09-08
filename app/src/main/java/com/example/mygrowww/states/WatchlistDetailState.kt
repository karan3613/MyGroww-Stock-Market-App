package com.example.mygrowww.states

import com.example.mygrowww.database.Stock

data class WatchlistDetailState (
    val isLoading : Boolean = false ,
    val error : String? = null ,
    val stocks : List<Stock> = emptyList() ,
    val watchlistName : String? = null
)