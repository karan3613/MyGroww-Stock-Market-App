package com.example.mygrowww.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygrowww.viewmodels.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllScreen(
    section: String,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateBack: () -> Boolean,
    onNavigateToProductDetails: (String) -> Unit
) {
    val state = viewModel.topGainersLosers.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (section) {
                            "top_gainers" -> "Top Gainers"
                            "top_losers" -> "Top Losers"
                            "most_active" -> "Most Active"
                            else -> "Stocks"
                        },
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: "Something went wrong",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    val stocks = when (section) {
                        "top_gainers" -> state.topGainersLosers?.top_gainers?.map {
                            StockUiModel(
                                ticker = it.ticker,
                                price = it.price,
                                change = it.change_percentage,
                                changeColor = Color(0xFF4CAF50) // Green
                            )
                        } ?: emptyList()
                        "top_losers" -> state.topGainersLosers?.top_losers?.map {
                            StockUiModel(
                                ticker = it.ticker,
                                price = it.price,
                                change = it.change_percentage,
                                changeColor = Color.Red
                            )
                        } ?: emptyList()
                        "most_active" -> state.topGainersLosers?.most_actively_traded?.map {
                            StockUiModel(
                                ticker = it.ticker,
                                price = it.price,
                                change = it.change_percentage,
                                changeColor = Color.Yellow
                            )
                        } ?: emptyList()
                        else -> emptyList()
                    }

                    if (stocks.isEmpty()) {
                        Text(
                            text = "No data available",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(stocks) { stock ->
                                StockCardWithArrow(
                                    stock = stock,
                                    onClick = { onNavigateToProductDetails(stock.ticker) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- STOCK CARD WITH ARROW ----------------- */
@Composable
fun StockCardWithArrow(stock: StockUiModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stock.ticker,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$${stock.price}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = stock.change,
                    color = stock.changeColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go to details",
                tint = Color.Yellow,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
