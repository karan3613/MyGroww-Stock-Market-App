package com.example.mygrowww.utils

import com.example.mygrowww.models.company_overview.CompanyOverview
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import com.example.mygrowww.states.CompanyOverviewState
import com.example.mygrowww.states.DailyState
import com.example.mygrowww.states.IntradayState
import com.google.gson.Gson
import java.io.File


fun parseCompanyData(
    companyOverviewFile: String =  "C:\\Users\\karan\\AndroidStudioProjects\\MyGrowww\\app\\src\\main\\java\\com\\example\\mygrowww\\CompanyOverviewFile",
): CompanyOverviewState {
    val gson = Gson()

    val companyOverview: CompanyOverview? = try {
        val json = File(companyOverviewFile).readText()
        gson.fromJson(json, CompanyOverview::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return CompanyOverviewState(
        companyOverview = companyOverview , false
    )
}

fun parseDailyData(
    dailyFile: String =  "C:\\Users\\karan\\AndroidStudioProjects\\MyGrowww\\app\\src\\main\\java\\com\\example\\mygrowww\\DailyFile"
) : DailyState{
    val gson = Gson()
    val daily: Daily? = try {
        val json = File(dailyFile).readText()
        gson.fromJson(json, Daily::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return DailyState(
        daily = daily , isLoading = false
    )
}
fun parseIntradayData(
    intradayFile: String =  "C:\\Users\\karan\\AndroidStudioProjects\\MyGrowww\\app\\src\\main\\java\\com\\example\\mygrowww\\IntradayFile"
) : IntradayState{
    val gson = Gson()
    val intraday: Intraday? = try {
        val json = File(intradayFile).readText()
        gson.fromJson(json, Intraday::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return IntradayState(
        intraday ,
        isLoading = false
    )
}

//DO NOT RUN THIS
//fun main() {
//    val companyOverview = parseCompanyData(
//        "C:\\Users\\karan\\AndroidStudioProjects\\MyGrowww\\app\\src\\main\\java\\com\\example\\mygrowww\\CompanyOverviewFile",
//    )
//
//    val daily = parseDailyData(
//        "C:\\Users\\karan\\AndroidStudioProjects\\MyGrowww\\app\\src\\main\\java\\com\\example\\mygrowww\\DailyFile"

