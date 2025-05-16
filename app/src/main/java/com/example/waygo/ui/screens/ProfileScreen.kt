package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R
import com.example.waygo.data.local.entity.UserEntity
import com.example.waygo.ui.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

@Composable
fun ProfileScreen(navController: NavController, user: UserEntity, viewModel: RegisterViewModel) {
    var username by remember { mutableStateOf(user.username)}
    var email by remember { mutableStateOf(user.email)}
    var birthdate by remember { mutableStateOf(user.birthdate)}
    var address by remember { mutableStateOf(user.address)}
    var country by remember { mutableStateOf(user.country)}
    var phone by remember { mutableStateOf(user.phone)}

    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = java.util.Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
            },
            java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
            java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
            java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    if (user.id == null || user.email.isBlank()) {
        // Guest detected, redirect to login
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Welcome, ${username}")
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }

            ) {
                Text(text = stringResource(id = R.string.logout))
            }
        }


        Text(text = stringResource(id = R.string.profile_screen))

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = stringResource(id = R.string.user)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(id = R.string.mail)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = selectedDate?.let { java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(it) }
                    ?: stringResource(id = R.string.birthdate)
            )
            birthdate = (selectedDate ?: java.util.Date())
        }
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(text = stringResource(id = R.string.address)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text(text = stringResource(id = R.string.country)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = stringResource(id = R.string.phone)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = viewModel.receiveEmail.value,
                onCheckedChange = { viewModel.receiveEmail.value = it }
            )
            Text(text = stringResource(id = R.string.receive_email) + " ${if (viewModel.receiveEmail.value) "Yes" else "No"}" )//TODO Traduct
        }

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
} //TODO: Add a button to save the changes