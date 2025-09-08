package com.example.mygrowww.states

import com.example.mygrowww.models.company_overview.CompanyOverview

data class CompanyOverviewState(
    val companyOverview: CompanyOverview? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
