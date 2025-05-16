package com.example.waygo.domain.model

data class AIRecommendations(
    val recommendationId: String,
    val tripId: String,
    val suggestedActivity: String,
    val rating: Double
) {

    //@TODO connectar amnb una API
    fun generateRecommendations() {
    }

    //@TODO actualitzar la puntuació
    fun rateRecommendation(newRating: Double) {
    }
}
