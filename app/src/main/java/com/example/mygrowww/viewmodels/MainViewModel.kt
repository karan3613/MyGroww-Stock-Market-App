package com.example.mygrowww.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygrowww.api.ApiRepository
import com.example.mygrowww.constants.Resource
import com.example.mygrowww.database.Stock
import com.example.mygrowww.database.StockDatabaseRepository
import com.example.mygrowww.states.TickerSearchState
import com.example.mygrowww.states.TopGainersLosersState
import com.example.mygrowww.states.WatchlistDetailState
import com.example.mygrowww.states.WatchlistState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//MAIN VIEWMODEL WITH STARTING SCREEN LOGIC AND DATABASE OPERATIONS LOGIC
@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: ApiRepository ,
    private val databaseRepository: StockDatabaseRepository
) : ViewModel(){

    private val _tickerSearch = mutableStateOf(TickerSearchState())
    val tickerSearch : State<TickerSearchState> = _tickerSearch

    private val _topGainersLosers = mutableStateOf(TopGainersLosersState())
    val topGainersLosers : State<TopGainersLosersState> = _topGainersLosers

    private val _watchlistState = mutableStateOf(WatchlistState())
    val watchlistState: State<WatchlistState> = _watchlistState
    private val _watchlistDetailState = mutableStateOf(WatchlistDetailState())
    val watchlistDetailState: State<WatchlistDetailState> = _watchlistDetailState

    init {
        getTopGainersLosers()
        getAllWatchlists()
    }

    fun loadWatchlistDetails(watchlistId: Long) {
        viewModelScope.launch {
            _watchlistDetailState.value = _watchlistDetailState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                val watchlistWithStocks = databaseRepository.getWatchlistWithStocks(watchlistId)
                _watchlistDetailState.value = _watchlistDetailState.value.copy(
                    isLoading = false,
                    stocks = watchlistWithStocks.stocks,
                    watchlistName = watchlistWithStocks.watchlist.name
                )
            } catch (e: Exception) {
                _watchlistDetailState.value = _watchlistDetailState.value.copy(
                    isLoading = false,
                    error = "Failed to load watchlist: ${e.message}"
                )
            }
        }
    }

    fun removeStockFromWatchlist(stock: Stock) {
        viewModelScope.launch {
            try {
                databaseRepository.deleteStock(stock)
                loadWatchlistDetails(stock.watchlistId)
            } catch (e: Exception) {
                _watchlistDetailState.value = _watchlistDetailState.value.copy(
                    error = "Failed to remove stock: ${e.message}"
                )
            }
        }
    }

    fun addStockToWatchlist(
        watchlistId: Long,
        symbol: String,
        companyName: String?,
        price: Double?,
        week52High: Double?,
        week52Low: Double?,
        marketCap: Double?,
        volume: Long?
    ) {
        viewModelScope.launch {
            try {
                databaseRepository.addStock(
                    watchlistId = watchlistId,
                    symbol = symbol,
                    companyName = companyName,
                    price = price,
                    week52High = week52High,
                    week52Low = week52Low,
                    marketCap = marketCap,
                    volume = volume
                )
                loadWatchlistDetails(watchlistId)
            } catch (e: Exception) {
                _watchlistDetailState.value = _watchlistDetailState.value.copy(
                    error = "Failed to add stock: ${e.message}"
                )
            }
        }
    }

    fun clearWatchlistDetailState() {
        _watchlistDetailState.value = WatchlistDetailState()
    }

    fun addWatchlist(name: String) {
        viewModelScope.launch {
            try {
                databaseRepository.addWatchlist(name)
                // Refresh list after adding
                getAllWatchlists()
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun getTickerSearch(symbol : String){
        viewModelScope.launch {
            _tickerSearch.value = tickerSearch.value.copy(
                isLoading = true
            )
            when(val response = apiRepository.getTickerSearch(symbol)){
                is Resource.Success -> {
                    _tickerSearch.value = tickerSearch.value.copy(
                        isLoading = false,
                        tickerSearch = response.data
                    )
                }
                is Resource.Error -> {
                    _tickerSearch.value = tickerSearch.value.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
                is Resource.Loading -> {
                    _tickerSearch.value = tickerSearch.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun getTopGainersLosers(){
        viewModelScope.launch {
            _topGainersLosers.value = topGainersLosers.value.copy(
                isLoading = true
            )
            when(val response = apiRepository.getTopGainersLosers()){
                is Resource.Success -> {
                    Log.d("TopGainersLosers" , response.data.toString())
                    _topGainersLosers.value = topGainersLosers.value.copy(
                        isLoading = false,
                        topGainersLosers = response.data
                    )
                }
                is Resource.Error -> {
                    _topGainersLosers.value = topGainersLosers.value.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
                is Resource.Loading -> {
                    _topGainersLosers.value = topGainersLosers.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }
    fun getAllWatchlists() {
        viewModelScope.launch {
            _watchlistState.value = watchlistState.value.copy(isLoading = true)
            try {
                val lists = databaseRepository.getAllWatchlists()
                _watchlistState.value = watchlistState.value.copy(
                    isLoading = false,
                    watchlists = lists
                )
            } catch (e: Exception) {
                _watchlistState.value = watchlistState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage
                )
            }
        }
    }
}