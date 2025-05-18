package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R

@Composable
fun CostsScreen(navController: NavController) {
    var accommodationCost by remember { mutableStateOf("") }
    var transportCost by remember { mutableStateOf("") }
    var foodCost by remember { mutableStateOf("") }
    var activitiesCost by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.budget_expenses))
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = accommodationCost,
            onValueChange = { accommodationCost = it },
            label = { Text(stringResource(id = R.string.accommodation_cost)) }
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = transportCost,
            onValueChange = { transportCost = it },
            label = { Text(stringResource(id = R.string.transport_cost)) }
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = foodCost,
            onValueChange = { foodCost = it },
            label = { Text(stringResource(id = R.string.food_cost)) }
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = activitiesCost,
            onValueChange = { activitiesCost = it },
            label = { Text(stringResource(id = R.string.activities_cost)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { /* Handle save action */ }) {
            Text(text = stringResource(id = R.string.save_costs))
        }
        Button(onClick = {
            navController.navigate("home") {
                popUpTo("back") { inclusive = true }
            }
        }) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}