package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R

@Composable
fun SettingsScreen(navController: NavController) {
    var selectedLanguage by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf("") }
    var fontSize by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.settings))
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = { selectedLanguage = it },
            label = { Text(stringResource(id = R.string.select_language)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.enable_notifications))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = selectedTheme,
            onValueChange = { selectedTheme = it },
            label = { Text(stringResource(id = R.string.select_theme)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = fontSize,
            onValueChange = { fontSize = it },
            label = { Text(stringResource(id = R.string.font_size)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Handle save settings action
        }) {
            Text(text = stringResource(id = R.string.save_settings))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("home") {
                popUpTo("back") { inclusive = true }
            }
        }) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}