package api

import android.util.Log
import com.example.mygrowww.constants.Resource
import com.example.mygrowww.models.company_overview.CompanyOverview
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import com.example.mygrowww.models.ticker_search.TickerSearch
import com.example.mygrowww.models.top_gainers_losers.TopGainersLosers
import retrofit2.HttpException
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api : API
){
    suspend fun getTickerSearch(keywords : String) : Resource<TickerSearch> {
        return try {
            val response = api.get_ticker_search(
                keywords = keywords
            )
            Resource.Success(response)
        }catch (e : HttpException){
            Log.d("apiError" , e.message())
            Resource.Loading(false)
            Resource.Error(e.message())
        }
    }
    suspend fun getCompanyOverview(symbol : String) : Resource<CompanyOverview> {
        return try {
            val response = api.get_company_overview(
                symbol = symbol
            )
            Resource.Success(response)
        }catch (e : HttpException){
            Log.d("apiError" , e.message())
            Resource.Loading(false)
            Resource.Error(e.message())
        }
    }
    suspend fun getTopGainersLosers() : Resource<TopGainersLosers> {
        return try {
            val response = api.get_top_gainers_losers()
            Log.d("top" , response.toString())
            Resource.Success(response)
        }catch (e : HttpException){
            Log.d("apiError" , e.message())
            Resource.Loading(false)
            Resource.Error(e.message())
        }
    }

    suspend fun getIntraday(symbol: String) : Resource<Intraday>{
        return try {
            val response = api.get_intraday(
                symbol =symbol
            )
            Resource.Success(response)
        }catch (e : HttpException){
            Log.d("apiError" , e.message())
            Resource.Loading(false)
            Resource.Error(e.message())
        }
    }

    suspend fun getDaily(symbol: String) : Resource<Daily>{
        return try {
            val response = api.get_daily(
                symbol =symbol
            )
            Resource.Success(response)
        }catch (e : HttpException){
            Log.d("apiError" , e.message())
            Resource.Loading(false)
            Resource.Error(e.message())
        }
    }
}