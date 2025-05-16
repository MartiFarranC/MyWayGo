package com.example.waygo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.waygo.BuildConfig
import com.example.waygo.ui.components.RoomImageCarouselWithControls
import com.example.waygo.ui.viewmodel.HotelDetailViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HotelDetailScreen {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelId: String,
    groupId: String,
    start: String,
    end: String,
    navController: NavHostController,
    vm: HotelDetailViewModel = hiltViewModel()
) {
    val ui = vm.uiState.collectAsState()
    val base = BuildConfig.HOTELS_API_URL.trimEnd('/')
    var showConfirmation by remember { mutableStateOf(false) }
    var showRoomImage by remember { mutableStateOf(false) }
    var imageToShow by remember { mutableStateOf<List<String>?>(null) }

    val dateFormat = DateTimeFormatter.ISO_DATE
    val nights = ChronoUnit.DAYS.between(LocalDate.parse(start, dateFormat), LocalDate.parse(end, dateFormat)).toInt()

    LaunchedEffect(hotelId) {
        vm.load(hotelId, groupId, start, end)
    }

    val selectedRoom = ui.value.selectedRoom
    val hotelId = ui.value.hotel?.id
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text((ui.value.hotel?.name + " ($hotelId)") ?: "Hotel")  },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Stay: $start → $end ($nights nights)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                ui.value.hotel?.imageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(base + it),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            items(ui.value.rooms ?: emptyList()) { room ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(room.roomType + " (${room.id})", fontWeight = FontWeight.Bold)
                        Text("${room.price} € / night")


                        Spacer(Modifier.height(8.dp))

                        room.images?.firstOrNull()?.let { img ->
                            Image(
                                painter = rememberAsyncImagePainter(base + img),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clickable {
                                        imageToShow = room.images.map { base + it }
                                        showRoomImage = true
                                    }
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        val total = room.price * nights
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column {
                                Text(
                                    "Total: €$total",
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {
                                vm.selectRoom(room)
                                showConfirmation = true
                            }) {
                                Text("Reserve")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRoomImage && imageToShow != null) {
        RoomImageCarouselWithControls(
            images = imageToShow ?: emptyList(),
            onDismiss = { showRoomImage = false }
        )
    }

    if (showConfirmation && selectedRoom != null) {
        val total = selectedRoom.price * nights
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    vm.reserveRoom(selectedRoom)
                    navController.popBackStack()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Reservation") },
            text = {
                Column {
                    Text("Hotel: ${ui.value.hotel?.name} (${ui.value.hotel?.id})")
                    Text("Room: ${selectedRoom.roomType} (${selectedRoom.id})")
                    Text("Price: €${selectedRoom.price} x $nights nights")
                    Text("Total: €$total")
                }
            }
        )
    }
}