package com.example.mygrowww.database

import androidx.room.Embedded
import androidx.room.Relation

data class WatchListWithStocks(
    @Embedded val watchlist: WatchList,
    @Relation(
        parentColumn = "id",
        entityColumn = "watchlistId"
    )
    val stocks: List<Stock>
)
