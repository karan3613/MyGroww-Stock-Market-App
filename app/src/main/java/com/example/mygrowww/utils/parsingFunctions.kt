package com.example.mygrowww.utils

import android.content.Context
import com.example.mygrowww.models.company_overview.CompanyOverview
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import com.example.mygrowww.states.CompanyOverviewState
import com.example.mygrowww.states.DailyState
import com.example.mygrowww.states.IntradayState
import com.google.gson.Gson

//DESIGNED TO AVOID IP ADDRESS RATE LIMITING FROM API
fun parseCompanyData(context: Context, fileName: String = "CompanyOverviewFile"): CompanyOverviewState {
    val gson = Gson()
    val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val companyOverview = gson.fromJson(json, CompanyOverview::class.java)
    return CompanyOverviewState(companyOverview = companyOverview, isLoading = false)
}

fun parseDailyData(context: Context, fileName: String = "DailyFile"): DailyState {
    val gson = Gson()
    val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val daily = gson.fromJson(json, Daily::class.java)
    return DailyState(daily = daily, isLoading = false)
}

fun parseIntradayData(context: Context, fileName: String = "IntradayFile"): IntradayState {
    val gson = Gson()
    val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val intraday = gson.fromJson(json, Intraday::class.java)
    return IntradayState(intraday = intraday, isLoading = false)
}
