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
fun HelpScreen(navController: NavController) {
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.help_and_feedback))
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = stringResource(id = R.string.help_topics))
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = stringResource(id = R.string.how_to_use_app))
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = stringResource(id = R.string.account_settings))
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = stringResource(id = R.string.privacy_policy))
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = stringResource(id = R.string.feedback))
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text(stringResource(id = R.string.your_feedback)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Handle feedback submission
        }) {
            Text(text = stringResource(id = R.string.submit_feedback))
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