package com.example.waygo.model

import java.util.Date

data class ItineraryItem(
    val id: String,
    val description: String,
    val date: Date,
    val location: String
)
