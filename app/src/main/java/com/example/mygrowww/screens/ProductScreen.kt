// ProductScreen.kt
package com.example.mygrowww.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygrowww.constants.ProductData
import com.example.mygrowww.database.WatchList
import com.example.mygrowww.models.company_overview.CompanyOverview
import com.example.mygrowww.models.daily.Daily
import com.example.mygrowww.models.intraday.Intraday
import com.example.mygrowww.utils.OHLCV
import com.example.mygrowww.utils.toOHLCV
import com.example.mygrowww.viewmodels.MainViewModel
import com.example.mygrowww.viewmodels.ProductViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    symbol: String = "IBM",
    productViewModel: ProductViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val companyState = productViewModel.company.value
    val dailyState = productViewModel.daily.value
    val intradayState = productViewModel.intraday.value
    val watchlistState = mainViewModel.watchlistState.value



    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSaveSheet by remember { mutableStateOf(false) }
    var newWatchlistName by remember { mutableStateOf(TextFieldValue("")) }
    var showCreateWatchlist by remember { mutableStateOf(false) }


    LaunchedEffect(symbol) {
        productViewModel.getDetails(symbol)
        mainViewModel.getAllWatchlists()
    }

    // Save to Watchlist Bottom Sheet
    if (showSaveSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSaveSheet = false
                showCreateWatchlist = false
                newWatchlistName = TextFieldValue("")
            },
            sheetState = sheetState,
            containerColor = Color.DarkGray.copy(alpha = 0.95f),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Save to Watchlist",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (showCreateWatchlist) {
                    // Create new watchlist form
                    OutlinedTextField(
                        value = newWatchlistName,
                        onValueChange = { newWatchlistName = it },
                        label = { Text("New Watchlist Name", color = Color.White) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Yellow,
                            unfocusedTextColor = Color.Yellow,
                            focusedBorderColor = Color.Yellow,
                            unfocusedBorderColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                showCreateWatchlist = false
                                newWatchlistName = TextFieldValue("")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.White)
                        }
                        Button(
                            onClick = {
                                if (newWatchlistName.text.isNotBlank()) {
                                    mainViewModel.addWatchlist(newWatchlistName.text.trim())
                                    newWatchlistName = TextFieldValue("")
                                    showCreateWatchlist = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Create", color = Color.Black)
                        }
                    }
                } else {
                    // List existing watchlists
                    if (watchlistState.watchlists.isEmpty()) {
                        Text(
                            "No watchlists available. Create one below.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        watchlistState.watchlists.forEach { watchlist ->
                            WatchlistItem(
                                watchlist = watchlist,
                                onSelect = {
                                    // Save stock to selected watchlist
                                    companyState.companyOverview?.let { company ->
                                        mainViewModel.addStockToWatchlist(
                                            watchlistId = watchlist.id,
                                            symbol = company.Symbol,
                                            companyName = company.Name,
                                            price = company.`52WeekHigh`.toDoubleOrNull(),
                                            week52High = company.`52WeekHigh`.toDoubleOrNull(),
                                            week52Low = company.`52WeekLow`.toDoubleOrNull(),
                                            marketCap = company.MarketCapitalization.toDoubleOrNull(),
                                            volume = null // Volume not available in company overview
                                        )
                                    }
                                    showSaveSheet = false
                                }
                            )
                        }
                    }

                    // Create new watchlist button
                    Button(
                        onClick = { showCreateWatchlist = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create New Watchlist", color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = companyState.companyOverview?.Symbol ?: "Stock Details",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Yellow
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSaveSheet = true }) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "Save to Watchlist",
                            tint = Color.Yellow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                companyState.isLoading -> {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                companyState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = companyState.error,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { productViewModel.getDetails(symbol) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                        ) {
                            Text("Retry", color = Color.Black)
                        }
                    }
                }
            companyState.companyOverview != null && dailyState.daily != null && intradayState.intraday != null -> {
                    CompanyDetailsContent(companyState.companyOverview , dailyState.daily, intradayState.intraday)
                }
            }
        }
    }
}

@Composable
fun ChartLoadingPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Price Chart",
                color = Color.Yellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Loading chart data...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    dailyData: List<OHLCV>,
    intradayData: List<OHLCV>
) {
    var chartType by remember { mutableStateOf("Candle") }
    var range by remember { mutableStateOf("1M") }

    // Filter data and ensure no nulls
    val quotes = remember(chartType, range, dailyData, intradayData) {
        val sourceData = when (range) {
            "1D" -> intradayData.takeLast(24)
            "1W" -> intradayData.takeLast(120)
            "15D" -> dailyData.takeLast(15)
            "1M" -> dailyData.takeLast(30)
            "3M" -> dailyData.takeLast(90)
            else -> dailyData
        }
        // Ensure data is valid and not empty
        sourceData.filter { it.open > 0 && it.high > 0 && it.low > 0 && it.close > 0 }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Price Chart",
                color = Color.Yellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Chart type buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { chartType = "Line" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chartType == "Line") Color.Yellow else Color.Gray
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        "Line",
                        color = if (chartType == "Line") Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { chartType = "Candle" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chartType == "Candle") Color.Yellow else Color.Gray
                    ),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        "Candle",
                        color = if (chartType == "Candle") Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Chart AndroidView
            if (quotes.isNotEmpty()) {
                key(chartType) {
                    AndroidView(
                        factory = { context ->
                            if (chartType == "Candle") {
                                CandleStickChart(context).apply {
                                    setupCandleChart(this)
                                }
                            } else {
                                LineChart(context).apply {
                                    setupLineChart(this)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        update = { chart ->
                            try {
                                if (chartType == "Candle" && chart is CandleStickChart) {
                                    val entries = quotes.mapIndexed { i, q ->
                                        CandleEntry(i.toFloat(), q.high, q.low, q.open, q.close)
                                    }
                                    val dataSet = CandleDataSet(entries, "Price").apply {
                                        decreasingColor = android.graphics.Color.RED
                                        increasingColor = android.graphics.Color.GREEN
                                        shadowColor = android.graphics.Color.GRAY
                                        shadowWidth = 1f
                                        setDrawValues(false)
                                        decreasingPaintStyle = android.graphics.Paint.Style.FILL
                                        increasingPaintStyle = android.graphics.Paint.Style.STROKE
                                    }
                                    chart.data = CandleData(dataSet)
                                    chart.notifyDataSetChanged()
                                    chart.invalidate()
                                } else if (chartType == "Line" && chart is LineChart) {
                                    val entries = quotes.mapIndexed { i, q ->
                                        Entry(i.toFloat(), q.close)
                                    }
                                    val dataSet = LineDataSet(entries, "Close").apply {
                                        color = android.graphics.Color.CYAN
                                        setCircleColor(android.graphics.Color.CYAN)
                                        lineWidth = 2f
                                        circleRadius = 4f
                                        setDrawCircleHole(false)
                                        setDrawValues(false)
                                        setDrawFilled(true)
                                        fillColor = android.graphics.Color.CYAN
                                        fillAlpha = 50
                                    }
                                    chart.data = LineData(dataSet)
                                    chart.notifyDataSetChanged()
                                    chart.invalidate()
                                }
                            } catch (e: Exception) {
                                Log.e("StockChart", "Chart update error: ${e.message}")
                            }
                        } ,

                        )
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available for $range",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // Range buttons
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("1D", "1W").forEach { r ->
                        Button(
                            onClick = { range = r },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (range == r) Color.Yellow else Color.Gray
                            ),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .padding(horizontal = 2.dp)
                        ) {
                            Text(
                                r,
                                color = if (range == r) Color.Black else Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier.fillMaxWidth().height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("15D", "1M", "3M").forEach { r ->
                        Button(
                            onClick = { range = r },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (range == r) Color.Yellow else Color.Gray
                            ),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .padding(horizontal = 2.dp)
                        ) {
                            Text(
                                r,
                                color = if (range == r) Color.Black else Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

private fun setupCandleChart(chart: CandleStickChart) {
    chart.apply {
        description.isEnabled = false
        setTouchEnabled(true)
        setScaleEnabled(true)
        setPinchZoom(true)
        setBackgroundColor(android.graphics.Color.TRANSPARENT)

        xAxis.apply {
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            textColor = android.graphics.Color.WHITE
            setDrawGridLines(false)
            granularity = 1f
            setLabelCount(5, true)
        }

        axisLeft.apply {
            textColor = android.graphics.Color.WHITE
            setDrawGridLines(true)
            gridColor = android.graphics.Color.GRAY
            setLabelCount(6, true)
        }

        axisRight.isEnabled = false
        legend.textColor = android.graphics.Color.WHITE
    }
}

private fun setupLineChart(chart: LineChart) {
    chart.apply {
        description.isEnabled = false
        setTouchEnabled(true)
        setScaleEnabled(true)
        setPinchZoom(true)
        setBackgroundColor(android.graphics.Color.TRANSPARENT)

        xAxis.apply {
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            textColor = android.graphics.Color.WHITE
            setDrawGridLines(false)
            granularity = 1f
            setLabelCount(5, true)
        }

        axisLeft.apply {
            textColor = android.graphics.Color.WHITE
            setDrawGridLines(true)
            gridColor = android.graphics.Color.GRAY
            setLabelCount(6, true)
        }

        axisRight.isEnabled = false
        legend.textColor = android.graphics.Color.WHITE
    }
}


@Composable
fun WatchlistItem(
    watchlist: WatchList,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Text(
            text = watchlist.name,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun CompanyDetailsContent(company: CompanyOverview, daily: Daily, intraday: Intraday) {

    val dailyData by remember(daily) {
        derivedStateOf {
            if (daily.timeSeries != null) {
                daily.toOHLCV().also { list ->
                    Log.d("OHLCV", "Daily data converted: ${list.size} entries")
                }
            } else {
                Log.d("OHLCV", "Daily timeSeries is null, returning empty list")
                emptyList()
            }
        }
    }

    val intradayData by remember(intraday) {
        derivedStateOf {
            if (intraday.timeSeries != null) {
                intraday.toOHLCV().also { list ->
                    Log.d("OHLCV", "Intraday data converted: ${list.size} entries")
                }
            } else {
                Log.d("OHLCV", "Intraday timeSeries is null, returning empty list")
                emptyList()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Company Header
        CompanyHeader(company)
        Log.d("daily" , dailyData.toString())
        Log.d("intraday" , intradayData.toString())

        if (dailyData.isNotEmpty() || intradayData.isNotEmpty()) {
            StockChart(
                modifier = Modifier.fillMaxWidth(),
                dailyData = dailyData,
                intradayData = intradayData
            )
        } else {
            // Show loading state for chart
            ChartLoadingPlaceholder()
        }

        // Key Metrics
        KeyMetricsSection(company)

        // Financial Ratios
        FinancialRatiosSection(company)

        // Company Info
        CompanyInfoSection(company)

        // Description
        if (company.Description.isNotBlank()) {
            DescriptionSection(company.Description)
        }
    }
}

@Composable
fun CompanyHeader(company: CompanyOverview) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = company.Name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${company.Symbol} • ${company.Exchange}",
                color = Color.Yellow,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${company.Industry} • ${company.Sector}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun KeyMetricsSection(company: CompanyOverview) {
    SectionCard(title = "Key Metrics") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Market Cap", formatLargeNumber(company.MarketCapitalization))
                MetricItem("P/E Ratio", company.PERatio)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("52W High", "$${company.`52WeekHigh`}", Color.Green)
                MetricItem("52W Low", "$${company.`52WeekLow`}", Color.Red)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Beta", company.Beta)
                MetricItem("EPS", company.EPS, getEPSColor(company.EPS))
            }
        }
    }
}

@Composable
fun FinancialRatiosSection(company: CompanyOverview) {
    SectionCard(title = "Financial Ratios") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("ROE", "${company.ReturnOnEquityTTM}%", getPercentageColor(company.ReturnOnEquityTTM))
                MetricItem("ROA", "${company.ReturnOnAssetsTTM}%", getPercentageColor(company.ReturnOnAssetsTTM))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Profit Margin", "${company.ProfitMargin}%", getPercentageColor(company.ProfitMargin))
                MetricItem("Book Value", "$${company.BookValue}")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Dividend Yield", "${company.DividendYield}%", if (company.DividendYield.toDoubleOrNull() != null && company.DividendYield.toDouble() > 0) Color.Green else Color.White)
                MetricItem("PEG Ratio", company.PEGRatio)
            }
        }
    }
}

@Composable
fun CompanyInfoSection(company: CompanyOverview) {
    SectionCard(title = "Company Information") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoRow("Country", company.Country)
            InfoRow("Currency", company.Currency)
            InfoRow("Fiscal Year End", company.FiscalYearEnd)
            if (company.OfficialSite.isNotBlank()) {
                InfoRow("Website", company.OfficialSite)
            }
        }
    }
}

@Composable
fun DescriptionSection(description: String) {
    SectionCard(title = "About Company") {
        Text(
            text = description,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = Color.Yellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
fun MetricItem(
    label: String,
    value: String,
    valueColor: Color = Color.White
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Text(
            text = if (value.isBlank() || value == "None") "N/A" else value,
            color = valueColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (value.isBlank()) "N/A" else value,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Helper functions
private fun formatLargeNumber(numberStr: String): String {
    val number = numberStr.toDoubleOrNull() ?: return "N/A"
    return when {
        number >= 1_000_000_000_000 -> "$${String.format("%.1f", number / 1_000_000_000_000)}T"
        number >= 1_000_000_000 -> "$${String.format("%.1f", number / 1_000_000_000)}B"
        number >= 1_000_000 -> "$${String.format("%.1f", number / 1_000_000)}M"
        number >= 1_000 -> "$${String.format("%.1f", number / 1_000)}K"
        else -> "$${String.format("%.0f", number)}"
    }
}

private fun getEPSColor(epsStr: String): Color {
    val eps = epsStr.toDoubleOrNull()
    return when {
        eps == null -> Color.White
        eps > 0 -> Color.Green
        eps < 0 -> Color.Red
        else -> Color.White
    }
}

private fun getPercentageColor(percentStr: String): Color {
    val percent = percentStr.replace("%", "").toDoubleOrNull()
    return when {
        percent == null -> Color.White
        percent > 0 -> Color.Green
        percent < 0 -> Color.Red
        else -> Color.White
    }
}