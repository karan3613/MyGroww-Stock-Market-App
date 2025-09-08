package com.example.mygrowww.constants

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mygrowww.states.CompanyOverviewState
import com.example.mygrowww.states.DailyState
import com.example.mygrowww.states.IntradayState

//IGNORE SAMPLE DATA MADE FOR TESTING
object ProductData {
    var companyOverview  by  mutableStateOf(CompanyOverviewState(companyOverview = null , isLoading = false , error = null))
    var intraday by mutableStateOf(IntradayState(intraday = null , isLoading = false , error = null))
    var daily by mutableStateOf(DailyState(daily = null , isLoading = false , error = null))
}