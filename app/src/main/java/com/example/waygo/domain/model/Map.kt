package com.example.waygo.domain.model

data class Map(
    val location: String
) {
    fun showLocation() {
        // Integració amb Google Maps
    }

    fun getNearbyPlaces() {
        // Obtenir llocs propers via API
    }
}
