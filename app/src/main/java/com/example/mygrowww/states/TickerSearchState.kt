package com.example.mygrowww.states

import com.example.mygrowww.models.ticker_search.TickerSearch

data class TickerSearchState(
    val isLoading: Boolean = false,
    val error : String? = null ,
    val tickerSearch : TickerSearch? = null
)
