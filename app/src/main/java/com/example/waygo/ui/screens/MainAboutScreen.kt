package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.about_us))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.team_info))
        Text(text = stringResource(id = R.string.team_members))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.version))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.summary))
        Text(text = stringResource(id = R.string.app_summary))
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