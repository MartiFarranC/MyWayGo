package com.example.waygo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Popup
import com.example.waygo.R
import com.example.waygo.data.local.dao.UserDao
import com.example.waygo.data.local.entity.UserEntity
import com.example.waygo.ui.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun RegisterScreen(navController: NavController, userDao: UserDao, viewModel: RegisterViewModel) {
    val email = viewModel.email.value
    val username = viewModel.username.value
    var birthdate by remember { mutableStateOf<Date?>(null) }
    var address by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var receiveEmail by remember { mutableStateOf(false) }
    var password = viewModel.password.value
    var confirmPassword by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    var showVerificationPrompt by remember { mutableStateOf(false) }
    var showContinueButton by remember { mutableStateOf(false) }
//    var hashedPassword by remember { mutableStateOf("") }

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

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.register_screen))

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
            Text(text = stringResource(id = R.string.receive_email) + " ${if (viewModel.receiveEmail.value) "Yes" else "No"}" )
        }

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

        var emailSent by remember { mutableStateOf(false) }
        var emailVerified by remember { mutableStateOf(false) }
        var step by remember { mutableStateOf(1) } // 1 = Email not verified, 2 = Email verified

        Button(onClick = {
            val auth = FirebaseAuth.getInstance()
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthdate.toString().isEmpty() || address.isEmpty() || country.isEmpty() || phone.isEmpty()) {
                errorMessage = context.getString(R.string.empty_fields)
                showMessage = true
            } else {
                if (password == confirmPassword) {
                    if(step == 1){
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser = task.result?.user
                                    val verifyEmailText = context.getString(R.string.verify_email)

                                    firebaseUser?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                                        if (emailTask.isSuccessful) {
                                            emailSent = true
                                            Toast.makeText(context, verifyEmailText, Toast.LENGTH_LONG).show()
                                        } else {
                                            errorMessage = context.getString(R.string.verification_failed)
                                            showMessage = true
                                        }
                                    }

                                    val userId = firebaseUser?.uid ?: ""
                                    val userEntity = UserEntity(
                                        id = userId,
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

                                    step = 2

                                } else {
                                    errorMessage = task.exception?.message ?: "Register error"
                                    showMessage = true
                                }
                            }
                    }else{
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.reload()?.addOnCompleteListener {
                            if (user != null && user.isEmailVerified) {
                                emailVerified = true
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                                step = 1
                            } else {
                                errorMessage = "Verification error"
                                showMessage = true
                            }
                        }
                    }
                } else {
                    errorMessage = "Passwords do not match"
                    showMessage = true
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

