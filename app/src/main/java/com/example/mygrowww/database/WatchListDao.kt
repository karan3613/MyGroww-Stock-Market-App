package com.example.mygrowww.database

import androidx.room.*

//DAO FOR HANDLING DATABASE QUERIES
@Dao
interface WatchListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: WatchList): Long

    @Delete
    suspend fun deleteWatchlist(watchlist: WatchList)

    @Query("SELECT * FROM watchlists")
    suspend fun getAllWatchlists(): List<WatchList>

    @Transaction
    @Query("SELECT * FROM watchlists WHERE id = :id")
    suspend fun getWatchlistWithStocks(id: Long): WatchListWithStocks

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stock): Long

    @Delete
    suspend fun deleteStock(stock: Stock)

    @Query("SELECT * FROM stocks WHERE watchlistId = :watchlistId")
    suspend fun getStocksForWatchlist(watchlistId: Long): List<Stock>
}
