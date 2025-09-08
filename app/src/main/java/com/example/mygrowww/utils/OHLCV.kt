package com.example.mygrowww.utils

data class OHLCV(
    val date: String,  // For Intraday include time as well, e.g., "2025-09-08 14:30"
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long
)