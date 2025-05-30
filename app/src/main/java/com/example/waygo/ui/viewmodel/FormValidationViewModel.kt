package com.example.waygo.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.waygo.R
import com.example.waygo.utils.FormValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FormValidationViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var storeName by mutableStateOf("")
        private set

    var storeLocation by mutableStateOf("")
        private set

    var mobile by mutableStateOf("")
        private set

    var pinCode by mutableStateOf("")
        private set

    // Mensajes de error (null si no hay error)
    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var storeNameError by mutableStateOf<String?>(null)
        private set

    var storeLocationError by mutableStateOf<String?>(null)
        private set

    var mobileError by mutableStateOf<String?>(null)
        private set

    var pinCodeError by mutableStateOf<String?>(null)
        private set


    // ======================
    //  Setters de cada campo
    // ======================
    fun onEmailChanged(newValue: String) {
        email = newValue
    }

    fun onPasswordChanged(newValue: String) {
        password = newValue
    }

    fun onConfirmPasswordChanged(newValue: String) {
        confirmPassword = newValue
    }

    fun onStoreNameChanged(newValue: String) {
        storeName = newValue
    }

    fun onStoreLocationChanged(newValue: String) {
        storeLocation = newValue
    }

    fun onMobileChanged(newValue: String) {
        mobile = newValue
    }

    fun onPinCodeChanged(newValue: String) {
        pinCode = newValue
    }

    // Llamado desde la UI al presionar "Registrar"
    @Composable
    fun onRegisterClicked(): Boolean {
        val allValid = validateAllFields()

        if (allValid) {
            Toast.makeText(context, "Perfect!", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    // ==================================
    //  Lógica para validar todos los campos
    // ==================================
    fun validateAllFields(): Boolean {
        val isEmailValid = FormValidationUtils.validateUserEmail(email)
        val isPasswordValid = FormValidationUtils.validatePassword(password)
        val isConfirmPasswordValid = FormValidationUtils.validateConfirmationPassword(password, confirmPassword)
        val isStoreNameValid = FormValidationUtils.validateStoreName(storeName)
        val isStoreLocationValid = FormValidationUtils.validateStoreLocation(storeLocation)
        val isMobileValid = FormValidationUtils.validateMobile(mobile)
        val isPinValid = FormValidationUtils.validatePin(pinCode)


        // Asignamos el recurso que corresponda según cada validación
        emailError = if (!isEmailValid) context.getString(R.string.error_invalid_email) else null
        passwordError = if (!isPasswordValid) context.getString(R.string.error_password_too_short) else null
        confirmPasswordError = if (!isConfirmPasswordValid) context.getString(R.string.error_password_mismatch) else null
        storeNameError = if (!isStoreNameValid) context.getString(R.string.error_required_field) else null
        storeLocationError = if (!isStoreLocationValid) context.getString(R.string.error_required_field) else null
        mobileError = if (!isMobileValid) context.getString(R.string.error_mobile_digits) else null
        pinCodeError = if (!isPinValid) context.getString(R.string.error_pin_digits) else null

        return isEmailValid && isPasswordValid && isConfirmPasswordValid &&
                isStoreNameValid && isStoreLocationValid &&
                isMobileValid && isPinValid
    }

}