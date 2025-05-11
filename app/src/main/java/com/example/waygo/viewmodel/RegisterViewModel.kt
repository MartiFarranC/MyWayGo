package com.example.waygo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var email = mutableStateOf("")
    var username = mutableStateOf("")
    var receiveEmail = mutableStateOf(false)
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var birthdate = mutableStateOf("")
    var address = mutableStateOf("")
    var country = mutableStateOf("")
    var phone = mutableStateOf("")
    var showMessage = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
}