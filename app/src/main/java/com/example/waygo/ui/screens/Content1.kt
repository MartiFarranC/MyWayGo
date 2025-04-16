package com.example.waygo.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Person
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.waygo.model.Trip
import com.example.waygo.viewmodel.TripViewModel
import androidx.compose.ui.window.Popup

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Content1(navController: NavController, paddingValues: PaddingValues, tripViewModel: TripViewModel) {
    val trips by tripViewModel.trips.collectAsState() // Observe trips from ViewModel
    var showDialog by remember { mutableStateOf(false) }
    var editDialog by remember { mutableStateOf(false) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }

    Box(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Button(
                    onClick = { navController.navigate(route = "profile") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.profile),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                trips.forEach { trip ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(
                                    text = trip.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Button(
                                    onClick = {
                                        editDialog = true
                                        selectedTrip = trip
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(id = R.string.profile),
                                    )
                                }
                            }
                            Text(
                                text = trip.destinations.replace("[", "").replace("]", ""),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = trip.participants.replace("[", "").replace("]", ""),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = trip.startDate,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = trip.endDate,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_trip)
            )
        }
    }

    if (showDialog) {
        TravelCreatorDialog(
            onDismiss = { showDialog = false },
            onSave = { trip ->
                tripViewModel.addTrip(trip) // Add trip to ViewModel
                showDialog = false
            }
        )
    }

    if (editDialog && selectedTrip != null) {
        TravelEditDialog(
            trip = selectedTrip!!,
            onDismiss = { editDialog = false },
            onSave = { updatedTrip ->
                tripViewModel.editTrip(updatedTrip)
                editDialog = false
            },
            onDelete = { trip ->
                tripViewModel.deleteTrip(trip)
                editDialog = false
            }
        )
    }
}

@Composable
fun TravelCreatorDialog(onDismiss: () -> Unit, onSave: (Trip) -> Unit) {
    var tripName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var newDestination by remember { mutableStateOf("") }
    var destinations by remember { mutableStateOf(listOf<String>()) }
    var newParticipants by remember { mutableStateOf("") }
    var participants by remember { mutableStateOf(listOf<String>()) }
    var showMessage by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.create_trip)) },
        text = {
            Column {
                OutlinedTextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    label = { Text(stringResource(id = R.string.trip_name)) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = newDestination,
                        onValueChange = { newDestination = it },
                        label = { Text(stringResource(id = R.string.destinations)) },
                        modifier = Modifier
                            .weight(1f) // Assigna pes per ocupar espai disponible
                            .padding(end = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            if (newDestination.isNotEmpty()) {
                                destinations = destinations + newDestination // Afegeix el nou participant a la llista
                                newDestination = "" // Reinicia el camp de text
                                showMessage = true
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_destination)
                        )
                    }
                }
                Text(
                    text = destinations.joinToString(", "),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (showMessage) {
                    Popup(
                        alignment = Alignment.Center,
                        onDismissRequest = { showMessage = false }
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Green)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.destination_added),
                                color = Color.White
                            )
                        }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(2000) // Mostra el missatge durant 2 segons
                            showMessage = false
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { startDatePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (startDate.isEmpty()) stringResource(id = R.string.start_date)
                        else startDate
                    )
                }

                Button(
                    onClick = { endDatePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (endDate.isEmpty()) stringResource(id = R.string.end_date)
                        else endDate
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newParticipants,
                        onValueChange = { newParticipants = it },
                        label = { Text(stringResource(id = R.string.participants)) },
                        modifier = Modifier
                            .weight(1f) // Assigna pes per ocupar espai disponible
                            .padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            if (newParticipants.isNotEmpty()) {
                                participants = participants + newParticipants // Afegeix el nou participant a la llista
                                newParticipants = "" // Reinicia el camp de text
                                showMessage = true
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_participant)
                        )
                    }
                }
                Text(
                    text = participants.joinToString(", "),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                if (tripName.isNotEmpty()) {
                    onSave(Trip(tripName, destinations.toString(),
                        participants.toString(), startDate, endDate))
                }
            }) {
                Text(text = stringResource(id = R.string.save_trip))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}


@Composable
fun TravelEditDialog( //Todo: S'ha de poder editar títol
    trip: Trip,
    onDismiss: () -> Unit,
    onSave: (Trip) -> Unit,
    onDelete: (Trip) -> Unit
) {
    var tripName by remember { mutableStateOf(trip.name) }
    var destinations by remember { mutableStateOf(trip.destinations.split(", ")) }
    var participants by remember { mutableStateOf(trip.participants.split(", ")) }
    var startDate by remember { mutableStateOf(trip.startDate) }
    var endDate by remember { mutableStateOf(trip.endDate) }
    var showDestinationsDialog by remember { mutableStateOf(false) }
    var showParticipantsDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.edit_trip))
                IconButton(onClick = { onDelete(trip) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_trip),
                        tint = Color.Red
                    )
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    label = { Text(stringResource(id = R.string.trip_name)) }
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showDestinationsDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.edit_destination))
                }

                Button(
                    onClick = { startDatePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (startDate.isEmpty()) stringResource(id = R.string.start_date)
                        else startDate
                    )
                }

                Button(
                    onClick = { endDatePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (endDate.isEmpty()) stringResource(id = R.string.end_date)
                        else endDate
                    )
                }

                Button(
                    onClick = { showParticipantsDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_participants)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (tripName.isNotEmpty()) {
                    onSave(Trip(tripName, destinations.joinToString(", "), participants.joinToString(", "), startDate, endDate))
                }
            }) {
                Text(text = stringResource(id = R.string.save_trip))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )

    if (showDestinationsDialog) {
        AlertDialog(
            onDismissRequest = { showDestinationsDialog = false },
            title = { Text(text = stringResource(id = R.string.destinations)) },
            text = {
                LazyColumn {
                    items(destinations.size) { index ->
                        val destination = destinations[index].replace("[", "").replace("]", "")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = destination, style = MaterialTheme.typography.bodyLarge)
                            IconButton(onClick = {
                                val updatedDestinations = destinations.toMutableList()
                                updatedDestinations.removeAt(index)
                                destinations = updatedDestinations
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = stringResource(id = R.string.delete_trip),
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDestinationsDialog = false }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            }
        )
    }

    if (showParticipantsDialog) {
        AlertDialog(
            onDismissRequest = { showParticipantsDialog = false },
            title = { Text(text = stringResource(id = R.string.participants)) },
            text = {
                LazyColumn {
                    items(participants.size) { index ->
                        val participant = participants[index].replace("[", "").replace("]", "")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = participant, style = MaterialTheme.typography.bodyLarge)
                            IconButton(onClick = {
                                val updatedParticipants = participants.toMutableList()
                                updatedParticipants.removeAt(index)
                                participants = updatedParticipants
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = stringResource(id = R.string.delete_trip),
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showParticipantsDialog = false }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            }
        )
    }
}