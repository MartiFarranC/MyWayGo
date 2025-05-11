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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PasswordRecoveryScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Function to send password recovery email
    fun sendPasswordRecoveryEmail() {
        if (email.isNotEmpty()) {
            loading = true
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    loading = false
                    if (task.isSuccessful) {
                        successMessage = "Password recovery email sent"
                        errorMessage = ""
                    } else {
                        successMessage = ""
                        errorMessage = task.exception?.message ?: "Error sending password recovery email"
                    }
                }
        } else {
            errorMessage = "Please enter your email"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.password_recovery), modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.mail)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { sendPasswordRecoveryEmail() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.recover_password))
        }

        // Show loading indicator
        if (loading) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.sending_recovery_email), modifier = Modifier.padding(8.dp))
        }

        // Display success or error message
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = androidx.compose.ui.graphics.Color.Red)
        }

        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = androidx.compose.ui.graphics.Color.Green)
        }
    }
}
