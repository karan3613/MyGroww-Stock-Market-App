package com.example.mygrowww.states

import com.example.mygrowww.models.top_gainers_losers.TopGainersLosers

data class TopGainersLosersState(
    val isLoading : Boolean = false ,
    val error : String? = null ,
    val topGainersLosers : TopGainersLosers? = null
)
