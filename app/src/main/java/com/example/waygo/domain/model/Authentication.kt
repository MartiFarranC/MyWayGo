package com.example.waygo.domain.model

data class Authentication(
    val email: String,
    val password: String
) {
    //@TODO fer la simulació d'autenticació
    fun login() {
    }

    //@TODO tanca la sessió
    fun logout() {
    }

    //@TODO enviar email de reset de contrasenya
    fun resetPassword() {
    }
}
