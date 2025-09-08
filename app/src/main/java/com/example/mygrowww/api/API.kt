package com.example.mygrowww.api

import com.example.mygrowww.constants.APIConstants
import com.example.mygrowww.models.company_overview.CompanyOverview
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import com.example.mygrowww.models.ticker_search.TickerSearch
import com.example.mygrowww.models.top_gainers_losers.TopGainersLosers
import retrofit2.http.GET
import retrofit2.http.Query


//API INTERFACE CONSISTING OF QUERY PARAMETERS AND PATH PARAMETERS
interface API {

    @GET("query")
    suspend fun get_ticker_search(
        @Query("function") function : String = APIConstants.TICKER_SEARCH ,
        @Query("apikey") apikey : String = APIConstants.API_KEY ,
        @Query("keywords") keywords : String
    ) : TickerSearch

    @GET("query")
    suspend fun get_top_gainers_losers(
        @Query("function") function : String = APIConstants.TOP_GAINERS_LOSERS ,
        @Query("apikey") apikey : String = APIConstants.API_KEY ,
    ) : TopGainersLosers

    @GET("query")
    suspend fun get_company_overview(
        @Query("function") function : String = APIConstants.COMPANY_OVERVIEW ,
        @Query("apikey") apikey : String = APIConstants.API_KEY ,
        @Query("symbol") symbol : String
    ) : CompanyOverview

    @GET("query")
    suspend fun get_intraday(
        @Query("function") function : String = APIConstants.INTRADAY ,
        @Query("apikey") apikey : String = APIConstants.API_KEY ,
        @Query("symbol") symbol : String ,
        @Query("interval") interval : String = "30min"
    ) : Intraday

    @GET("query")
    suspend fun get_daily(
        @Query("function") function : String = APIConstants.DAILY ,
        @Query("apikey") apikey : String = APIConstants.API_KEY ,
        @Query("symbol") symbol : String
    ) : Daily
}