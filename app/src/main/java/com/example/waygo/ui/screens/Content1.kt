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
                                    }, //TODO boto
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(id = R.string.profile),
                                    )
                                }
                            }
                            Text(
                                text = trip.destinations,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = trip.participants,
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
    var destinations by remember { mutableStateOf("") }
    var participants by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

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

                OutlinedTextField(
                    value = destinations,
                    onValueChange = { destinations = it },
                    label = { Text(stringResource(id = R.string.destinations)) }
                )
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

                OutlinedTextField(
                    value = participants,
                    onValueChange = { participants = it },
                    label = { Text(stringResource(id = R.string.participants)) }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (tripName.isNotEmpty()) {
                    onSave(Trip(tripName, destinations, participants, startDate, endDate))
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
fun TravelEditDialog(
    trip: Trip,
    onDismiss: () -> Unit,
    onSave: (Trip) -> Unit,
    onDelete: (Trip) -> Unit
) {
    var tripName by remember { mutableStateOf(trip.name) }
    var destinations by remember { mutableStateOf(trip.destinations) }
    var participants by remember { mutableStateOf(trip.participants) }
    var startDate by remember { mutableStateOf(trip.startDate) }
    var endDate by remember { mutableStateOf(trip.endDate) }

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
            ){
                Text(text = stringResource(id = R.string.edit_trip))

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .clickable {
                            onDelete(trip) // Use the onDelete callback to delete the trip
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_trip),
                        tint = Color.White // Optional: Add tint for better visibility
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

                OutlinedTextField(
                    value = destinations,
                    onValueChange = { destinations = it },
                    label = { Text(stringResource(id = R.string.destinations)) }
                )
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

                OutlinedTextField(
                    value = participants,
                    onValueChange = { participants = it },
                    label = { Text(stringResource(id = R.string.participants)) }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (tripName.isNotEmpty()) {
                    onSave(Trip(tripName, destinations, participants, startDate, endDate))
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