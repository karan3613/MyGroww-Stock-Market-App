package com.example.mygrowww.states

import com.example.mygrowww.models.daily.Daily

data class DailyState (
    val isLoading : Boolean = false,
    val daily: Daily? = null,
    val error: String? = null
)