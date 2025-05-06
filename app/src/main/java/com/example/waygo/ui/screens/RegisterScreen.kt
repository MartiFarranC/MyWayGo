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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AuthState
import kotlinx.coroutines.launch
import java.security.MessageDigest


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun RegisterScreen(navController: NavController, userDao: UserDao) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.register_screen))
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.mail)) }
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
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val auth = FirebaseAuth.getInstance()
            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            errorMessage = task.exception?.message ?: "Registration failed"
                            showMessage = true
                        }
                    }
            } else {
                errorMessage = context.getString(R.string.password_error)
                showMessage = true
            }
        }) {
            Text(text = stringResource(id = R.string.register))
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
}