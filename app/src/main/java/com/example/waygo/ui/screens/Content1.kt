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
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.N) //TODO 24?
@Composable
fun Content1(navController: NavController, paddingValues: PaddingValues) {
    var showDialog by remember { mutableStateOf(false) }
    var tripName by remember { mutableStateOf("") }
    var destinations by remember { mutableStateOf("") }
    var travelDates by remember { mutableStateOf("") }
    var participants by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { navController.navigate(route = "travel1") }) {
                    Text(text = stringResource(id = R.string.travel_1))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(route = "travel2") }) {
                    Text(text = stringResource(id = R.string.travel_2))
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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(id = R.string.create_trip)) },
            text = {
                Column {
                    Text(text = stringResource(id = R.string.create_trip))
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = tripName,
                        onValueChange = { tripName = it },
                        label = { Text(stringResource(id = R.string.trip_name)) }
                    )
                    OutlinedTextField(
                        value = destinations,
                        onValueChange = { destinations = it },
                        label = { Text(stringResource(id = R.string.destinations)) }
                    )

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
                    // Aquí pots gestionar la informació introduïda
                    showDialog = false
                }) {
                    Text(text = stringResource(id = R.string.save_trip))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}