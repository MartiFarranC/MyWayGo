package com.example.waygo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Popup
import com.example.waygo.R
import com.example.waygo.dao.UserDao
import com.example.waygo.entity.UserEntity
import com.example.waygo.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Date

@Composable
fun SecondRegisterScreen(navController: NavController, userDao: UserDao, viewModel: RegisterViewModel) {
    val email = viewModel.email.value
    val username = viewModel.username.value
    val receiveEmail = viewModel.receiveEmail.value
    var birthdate by remember { mutableStateOf<Date?>(null) }
    var address by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val password = viewModel.password.value

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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.register_screen))
        Text("Email: $email")
        Text("Username: $username")
        Spacer(modifier = Modifier.height(20.dp))

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
            label = { Text(stringResource(id = R.string.address)) },
        )
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text(stringResource(id = R.string.country)) },
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(id = R.string.phone)) },
        )

        Button(onClick = {
            if (birthdate.toString().isEmpty() || address.isEmpty() || country.isEmpty() || phone.isEmpty()) {
                errorMessage = context.getString(R.string.empty_fields)
                showMessage = true
            } else {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val userEntity = UserEntity(
                        id = currentUser.uid,
                        email = email,
                        username = username,
                        birthdate = birthdate ?: Date(),
                        address = address,
                        country = country,
                        phone = phone,
                        receiveEmail = receiveEmail
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.insertUser(userEntity)
                    }
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                } else {
                    errorMessage = "User not logged in or session expired."
                    showMessage = true
                }
            }
        }) {
            Text(text = stringResource(id = R.string.register))
        }

    }

    if (showMessage) {
        Popup(
            alignment = Alignment.TopCenter,
            onDismissRequest = { showMessage = false }
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .background(Color.Red.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = Color.White
                )
            }
        }
    }
}