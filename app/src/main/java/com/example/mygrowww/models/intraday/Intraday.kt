package com.example.mygrowww.models.intraday

import com.google.gson.annotations.SerializedName

data class Intraday(
    @SerializedName("Meta Data")
    val metaData: IntradayMetaData?,

    @SerializedName("Time Series (30min)")
    val timeSeries: Map<String, IntradayStockData>?
)




