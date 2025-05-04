package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import com.example.waygo.R
import com.example.waygo.dao.UserDao
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController,  userDao: UserDao) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    // Llista d'usuaris de prova
//    val testUsers = mapOf(
//        "user1" to "password1",
//        "user2" to "password2"
//    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.login_screen))
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.user)) }
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(20.dp))
        val coroutineScope = rememberCoroutineScope()
        Button(onClick = {
            coroutineScope.launch {
                val user = userDao.getUserByEmail(username)
                if (user != null && user.hashedPassword == hashPassword(password)) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    showMessage = true
                }
            }
        }) {
            Text(text = stringResource(id = R.string.login))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.dont_have_account))
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { navController.navigate("register") }) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }

    if (showMessage) {
        Popup(
            alignment = Alignment.TopCenter,
            onDismissRequest = { showMessage = false }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Red.copy(alpha = 0.8f), shape =  RoundedCornerShape(8.dp)) // Color translúcid i puntes rodones
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.incorrect_credentials),
                    color = Color.White
                )
            }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000) // Mostra el missatge durant 2 segons
                showMessage = false
            }
        }

    }
}