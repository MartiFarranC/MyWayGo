package com.example.waygo.data.remote.dto

data class ReservationResponseDto(
    val message: String,
    val nights: Int,
    val reservation: ReservationDto
)

