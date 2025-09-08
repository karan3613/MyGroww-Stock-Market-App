package com.example.mygrowww.database

import javax.inject.Inject


//REPOSITORY FOR THE DATABASE TO ACCESS THE DAO
class StockDatabaseRepository @Inject constructor(
    private val dao: WatchListDao
            ) {

    suspend fun addWatchlist(name: String): Long {
        return dao.insertWatchlist(WatchList(name = name))
    }

    suspend fun deleteWatchlist(watchlist: WatchList) = dao.deleteWatchlist(watchlist)

    suspend fun getAllWatchlists(): List<WatchList> = dao.getAllWatchlists()

    suspend fun getWatchlistWithStocks(id: Long): WatchListWithStocks =
        dao.getWatchlistWithStocks(id)

    suspend fun addStock(
        watchlistId: Long,
        symbol: String,
        companyName: String?,
        price: Double?,
        week52High: Double?,
        week52Low: Double?,
        marketCap: Double?,
        volume: Long?
    ): Long {
        val stock = Stock(
            watchlistId = watchlistId,
            symbol = symbol,
            companyName = companyName,
            price = price,
            week52High = week52High,
            week52Low = week52Low,
            marketCap = marketCap,
            volume = volume
        )
        return dao.insertStock(stock)
    }

    suspend fun deleteStock(stock: Stock) = dao.deleteStock(stock)

    suspend fun getStocksForWatchlist(watchlistId: Long): List<Stock> =
        dao.getStocksForWatchlist(watchlistId)
}
