package com.example.mygrowww.states

import com.example.mygrowww.models.intraday.Intraday

data class IntradayState(
    val intraday : Intraday? = null ,
    val isLoading : Boolean = false ,
    val error : String? = null
)
