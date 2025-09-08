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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mygrowww.database.WatchList
import com.example.mygrowww.viewmodels.MainViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToWatchListDetails: (Long) -> Unit
) {
    val state = viewModel.watchlistState.value

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSheet by remember { mutableStateOf(false) }
    var watchlistName by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    // Only show the ModalBottomSheet when showSheet is true
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                watchlistName = TextFieldValue("") // Reset form when dismissed
            },
            sheetState = sheetState,
            containerColor = Color.DarkGray.copy(alpha = 0.9f),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Create New Watchlist", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    value = watchlistName,
                    onValueChange = { watchlistName = it },
                    label = { Text("Watchlist Name", color = Color.White , fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Yellow,
                        unfocusedTextColor = Color.Yellow,
                        focusedBorderColor = Color.Yellow,
                        unfocusedBorderColor = Color.White
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            showSheet = false
                            watchlistName = TextFieldValue("")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f) ,
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                    Button(
                        onClick = {
                            if (watchlistName.text.isNotBlank()) {
                                viewModel.addWatchlist(watchlistName.text.trim())
                                watchlistName = TextFieldValue("")
                                showSheet = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("Create", color = Color.Black)
                    }
                }
                // Add bottom padding to avoid gesture area
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Watchlists", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    IconButton(onClick = { showSheet = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Watchlist", tint = Color.Yellow)
                    }
                }
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = Color.Yellow,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: "Error loading watchlists",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.watchlists.isEmpty() -> {
                    Text(
                        text = "No watchlists yet. Add one from a product screen!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.watchlists) { watchlist ->
                            WatchlistRow(
                                watchlist = watchlist,
                                onClick = { onNavigateToWatchListDetails(watchlist.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistRow(watchlist: WatchList, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = watchlist.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go to watchlist",
                tint = Color.Yellow,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
