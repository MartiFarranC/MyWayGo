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
import com.example.waygo.entity.UserEntity

@Composable
fun ProfileScreen(navController: NavController, user: UserEntity) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (user == null) {
        Text("Error profile")
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${user.username}")
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.align(Alignment.End)        ) {
            Text(text = stringResource(id = R.string.logout))
        }

        Text(text = stringResource(id = R.string.profile_screen))

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = user.username,
            onValueChange = { user.username = it },
            label = { Text(text = stringResource(id = R.string.user)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user.email,
            onValueChange = { user.email = it },
            label = { Text(text = stringResource(id = R.string.mail)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Username: ${user.username}")
        Text(text = "Birthdate: ${user.birthdate}")
        Text(text = "Address: ${user.address}")
        Text(text = "Country: ${user.country}")
        Text(text = "Phone: ${user.phone}")
        Text(text = "Receive Emails: ${if (user.receiveEmail) "Yes" else "No"}")

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.back_to_home))
        }
    }
}