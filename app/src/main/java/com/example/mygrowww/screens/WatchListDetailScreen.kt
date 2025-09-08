package com.example.mygrowww.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygrowww.database.Stock
import com.example.mygrowww.viewmodels.MainViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistDetailScreen(
    watchlistId: Long,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit
) {
    val state = viewModel.watchlistDetailState.value
    // Load watchlist details when screen opens
    LaunchedEffect(watchlistId) {
        viewModel.loadWatchlistDetails(watchlistId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.watchlistName ?: "Watchlist Details",
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
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { viewModel.loadWatchlistDetails(watchlistId) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                        ) {
                            Text("Retry", color = Color.Black)
                        }
                    }
                }
                state.stocks.isEmpty() -> {
                    Text(
                        text = "No stocks in this watchlist yet.\nAdd stocks from the search screen!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.stocks) { stock ->
                            StockCard(
                                stock = stock,
                                onDeleteClick = {
                                    viewModel.removeStockFromWatchlist(stock)
                                } ,
                                onClick = {onNavigateToProductDetails(stock.symbol)}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockCard(
    stock: Stock,
    onDeleteClick: () -> Unit ,
    onClick : ()-> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {onClick},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with symbol and delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = stock.symbol,
                    color = Color.Yellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove stock",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Company name
            Text(
                text = stock.companyName ?: "Unknown Company",
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price
            Text(
                text = stock.price?.let { "₹${formatNumber(it)}" } ?: "N/A",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // 52 Week Range
            if (stock.week52High != null && stock.week52Low != null) {
                Text(
                    text = "52W: ₹${formatNumber(stock.week52Low)} - ₹${formatNumber(stock.week52High)}",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Market Cap and Volume row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Market Cap",
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                    Text(
                        text = stock.marketCap?.let { formatLargeNumber(it) } ?: "N/A",
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Volume",
                        color = Color.Gray,
                        fontSize = 9.sp
                    )
                    Text(
                        text = stock.volume?.let { formatLargeNumber(it.toDouble()) } ?: "N/A",
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


private fun formatNumber(number: Double): String {
    return NumberFormat.getNumberInstance(Locale("en", "IN")).format(number)
}

private fun formatLargeNumber(number: Double): String {
    return when {
        number >= 1_00_00_000 -> "₹${String.format("%.1f", number / 1_00_00_000)}Cr"
        number >= 1_00_000 -> "₹${String.format("%.1f", number / 1_00_000)}L"
        number >= 1_000 -> "₹${String.format("%.1f", number / 1_000)}K"
        else -> "₹${String.format("%.0f", number)}"
    }
}
