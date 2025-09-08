package com.example.mygrowww.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WatchList::class, Stock::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchListDao
}
