package com.example.waygo.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val profilePictureUrl: String?,
    val bio: String?,
    val followers: Int = 0,
    val following: Int = 0,
    val uploadedRoutes: Int = 0,
    val totalLikes: Int = 0
) {
    fun register() {
        // Aquí aniria la lògica de registre (ex. crida a una API o Firebase)
    }

    fun login(email: String, password: String) {
        // Lògica d'autenticació
    }

    fun logout() {
        // Destruir sessió de l'usuari
    }
}
