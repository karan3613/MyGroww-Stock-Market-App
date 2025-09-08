package com.example.mygrowww.models.daily

import com.google.gson.annotations.SerializedName

// Meta Data
data class DailyMetaData(
    @SerializedName("1. Information") val information: String?,
    @SerializedName("2. Symbol") val symbol: String?,
    @SerializedName("3. Last Refreshed") val lastRefreshed: String?,
    @SerializedName("4. Output Size") val outputSize: String?,
    @SerializedName("5. Time Zone") val timeZone: String?
)

// Each daily record (open, high, low, close, volume)



