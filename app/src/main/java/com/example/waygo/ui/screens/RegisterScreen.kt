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
import androidx.compose.material3.Checkbox
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
import kotlinx.coroutines.withContext
import java.security.MessageDigest


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun RegisterScreen(navController: NavController, userDao: UserDao, viewModel: RegisterViewModel) {
    val email = viewModel.email.value
    val username = viewModel.username.value

    var receiveEmail by remember { mutableStateOf(false) }
    var password = viewModel.password.value
    var confirmPassword by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    var showVerificationPrompt by remember { mutableStateOf(false) }
    var showContinueButton by remember { mutableStateOf(false) }
//    var hashedPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.register_screen))
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.username.value = it },
            label = { Text(stringResource(id = R.string.username)) },
        )

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text(stringResource(id = R.string.mail)) }
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

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(onClick = {
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                errorMessage = context.getString(R.string.empty_fields)
                showMessage = true
            } else if (!isValidEmail(email)) {
                errorMessage = context.getString(R.string.invalid_email)
                showMessage = true
            } else if (password != confirmPassword) {
                errorMessage = context.getString(R.string.passwords_do_not_match)
                showMessage = true
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val isTaken = userDao.isUsernameTaken(username) > 0
                    if (isTaken) {
                        errorMessage = context.getString(R.string.username_taken)
                        showMessage = true
                    } else {
                        withContext(Dispatchers.Main) {
                            navController.navigate("second_register")
                        }
                    }
                }
            }
        }) {
            Text(text = stringResource(id = R.string.next))
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Afegeix un petit marge
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd), // Alinea el contingut a baix a la dreta
            verticalAlignment = Alignment.CenterVertically // Centra verticalment el text i el botó
        ) {
            Text(text = stringResource(id = R.string.have_account))
            Spacer(modifier = Modifier.width(8.dp)) // Espai entre el text i el botó
            Button(onClick = { navController.navigate("login") }) {
                Text(text = stringResource(id = R.string.login))
            }
        }
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { navController.navigate("terms") }) {
                Text(text = stringResource(id = R.string.terms_and_conditions))
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
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                showMessage = false
            }
        }
    }

    if (showVerificationPrompt) {
        Text(
            text = stringResource(id = R.string.verification_email_sent),
            color = Color.Blue,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    if (showContinueButton) {
        Button(onClick = {
            val user = FirebaseAuth.getInstance().currentUser
            user?.reload()?.addOnCompleteListener {
                if (user.isEmailVerified) {
                    navController.navigate("second_register")
                } else {
                    errorMessage = context.getString(R.string.email_not_verified)
                    showMessage = true
                }
            }
        }) {
            Text(text = stringResource(id = R.string.continue_button))
        }
    }
}

