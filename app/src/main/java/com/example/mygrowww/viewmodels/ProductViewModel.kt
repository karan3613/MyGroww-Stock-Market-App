package com.example.mygrowww.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygrowww.api.ApiRepository
import com.example.mygrowww.constants.Resource
import com.example.mygrowww.states.CompanyOverviewState
import com.example.mygrowww.states.DailyState
import com.example.mygrowww.states.IntradayState

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

//VIEWMODEL FOR PRODUCT FETCHING AND USE FOR DISPLAYING ON THE PRODUCT SCREEN
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val apiRepository: ApiRepository ,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _company = mutableStateOf(CompanyOverviewState())
    val company : State<CompanyOverviewState> = _company

    private val _daily = mutableStateOf(DailyState())
    val daily : State<DailyState> = _daily

    private val _intraday = mutableStateOf(IntradayState())
    val intraday : State<IntradayState> = _intraday

//    init {
//        viewModelScope.launch {
//            _company.value = parseCompanyData(context)
//            _daily.value = parseDailyData(context)
//            _intraday.value = parseIntradayData(context)
//        }
//    }

    fun getDetails(symbol : String = "IBM"){
        getCompanyOverview(symbol)
        getDaily(symbol)
        getIntraday(symbol)
    }
    fun getCompanyOverview(symbol: String ){
        viewModelScope.launch {
            _company.value = CompanyOverviewState(isLoading = true)
            when(val response = apiRepository.getCompanyOverview(symbol)){
                is Resource.Error -> {
                    _company.value = CompanyOverviewState(error = response.message , isLoading = false)
                }
                is Resource.Loading -> {
                    _company.value = CompanyOverviewState(isLoading = true)
                }
                is Resource.Success -> {
                    Log.d("CompanyOverView" , response.data.toString())
                    _company.value = CompanyOverviewState(companyOverview = response.data , isLoading = false)
                }
            }
        }
    }

    fun getDaily(symbol: String ){
        viewModelScope.launch {
            _daily.value = DailyState(isLoading = true)
            when(val response = apiRepository.getDaily(symbol)){
                is Resource.Error -> {
                    _daily.value = DailyState(error = response.message , isLoading = false)
                }
                is Resource.Loading -> {
                    _daily.value = DailyState(isLoading = true)
                }
                is Resource.Success -> {
                    Log.d("DailyState" , response.data.toString())
                    _daily.value = DailyState(daily = response.data , isLoading = false )
                }
            }
        }
    }

    fun getIntraday(symbol: String ){
        viewModelScope.launch {
            _intraday.value = IntradayState(isLoading = true)
            when(val response = apiRepository.getIntraday(symbol)){
                is Resource.Error -> {
                    _intraday.value = IntradayState(error = response.message ,isLoading = false)
                }
                is Resource.Loading -> {
                    _intraday.value = IntradayState(isLoading = true)
                }
                is Resource.Success -> {
                    Log.d("IntradayState" , response.data.toString())
                    _intraday.value = IntradayState(intraday = response.data ,isLoading = false)
                }
            }
        }
    }
}