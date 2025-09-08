package com.example.mygrowww.models.daily

import com.example.mygrowww.utils.OHLCV
import com.example.mygrowww.models.intraday.Intraday
import com.google.gson.annotations.SerializedName

// Full API response
data class Daily(
    @SerializedName("Meta Data") val metaData: DailyMetaData?,
    @SerializedName("Time Series (Daily)") val timeSeries: Map<String, DailyStockData>?
)




