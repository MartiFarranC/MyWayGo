package com.example.waygo.data.remote.dto

data class ReserveRequestDto(
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String
)