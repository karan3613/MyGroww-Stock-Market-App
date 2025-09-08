package com.example.mygrowww.utils

import com.example.mygrowww.utils.OHLCV
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import kotlin.collections.component1
import kotlin.collections.component2

fun Intraday.toOHLCV(): List<OHLCV> =
    timeSeries?.mapNotNull { (date, data) ->
        try {
            OHLCV(
                date = date,
                open = data.open?.toFloatOrNull() ?: return@mapNotNull null,
                high = data.high?.toFloatOrNull() ?: return@mapNotNull null,
                low = data.low?.toFloatOrNull() ?: return@mapNotNull null,
                close = data.close?.toFloatOrNull() ?: return@mapNotNull null,
                volume = data.volume?.toLongOrNull() ?: 0L
            )
        } catch (_: Exception) { null }
    }?.sortedBy { it.date } ?: emptyList()

fun Daily.toOHLCV(): List<OHLCV> =
    timeSeries?.mapNotNull { (date, data) ->
        try {
            OHLCV(
                date = date,
                open = data.open?.toFloatOrNull() ?: return@mapNotNull null,
                high = data.high?.toFloatOrNull() ?: return@mapNotNull null,
                low = data.low?.toFloatOrNull() ?: return@mapNotNull null,
                close = data.close?.toFloatOrNull() ?: return@mapNotNull null,
                volume = data.volume?.toLongOrNull() ?: 0L
            )
        } catch (_: Exception) { null }
    }?.sortedBy { it.date } ?: emptyList()