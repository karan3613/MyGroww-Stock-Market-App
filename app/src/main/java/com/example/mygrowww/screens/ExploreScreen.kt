package com.example.mygrowww.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygrowww.viewmodels.MainViewModel

@Composable
fun ExploreScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToViewAll: (String) -> Unit ,
    onNavigateToProductDetails: (String ) -> Unit
) {
    val state = viewModel.topGainersLosers.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
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
                state.topGainersLosers?.let { data ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // SECTION 1 : Top Gainers
                        item {
                            SectionHeader(
                                title = "Top Gainers",
                                color = Color(0xFF4CAF50), // Green
                                onViewAll = { onNavigateToViewAll("top_gainers") }
                            )
                            StockGrid(
                                stocks = data.top_gainers.take(4).map {
                                    StockUiModel(
                                        ticker = it.ticker,
                                        price = it.price,
                                        change = it.change_percentage,
                                        changeColor = Color(0xFF4CAF50) // Green
                                    )
                                },
                                onNavigateToProductDetails = {symbol->
                                    onNavigateToProductDetails(symbol)
                                }
                            )
                        }

                        // SECTION 2 : Top Losers
                        item {
                            SectionHeader(
                                title = "Top Losers",
                                color = Color.Red,
                                onViewAll = { onNavigateToViewAll("top_losers") }
                            )
                            StockGrid(
                                stocks = data.top_losers.take(4).map {
                                    StockUiModel(
                                        ticker = it.ticker,
                                        price = it.price,
                                        change = it.change_percentage,
                                        changeColor = Color.Red
                                    )
                                }
                                ,
                                onNavigateToProductDetails = {symbol->
                                    onNavigateToProductDetails(symbol)
                                }
                            )
                        }

                        // SECTION 3 : Most Actively Traded
                        item {
                            SectionHeader(
                                title = "Most Active",
                                color = Color.Yellow,
                                onViewAll = { onNavigateToViewAll("most_active") }
                            )
                            StockGrid(
                                stocks = data.most_actively_traded.take(4).map {
                                    StockUiModel(
                                        ticker = it.ticker,
                                        price = it.price,
                                        change = it.change_percentage,
                                        changeColor = Color.Yellow
                                    )
                                } ,
                                onNavigateToProductDetails = {symbol->
                                    onNavigateToProductDetails(symbol)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/* -------------------- STOCK GRID ---------------------- */
@Composable
fun StockGrid(stocks: List<StockUiModel> , onNavigateToProductDetails: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(220.dp), // Show 2x2 grid
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false
    ) {
        items(stocks) { stock ->
            StockCard(stock , onClick = {
                onNavigateToProductDetails(stock.ticker)
            })
        }
    }
}

/* -------------------- STOCK CARD ---------------------- */
@Composable
fun StockCard(stock: StockUiModel , onClick : ()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            ,
        onClick = {onClick} , 
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
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
    }
}

/* -------------------- SECTION HEADER ---------------------- */
@Composable
fun SectionHeader(title: String, color: Color, onViewAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onViewAll) {
            Text(
                text = "View All",
                color = Color.Yellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* -------------------- UI MODEL ---------------------- */
data class StockUiModel(
    val ticker: String,
    val price: String,
    val change: String,
    val changeColor: Color
)
