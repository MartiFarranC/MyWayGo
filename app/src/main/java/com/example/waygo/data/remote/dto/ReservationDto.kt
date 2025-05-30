package com.example.waygo.data.remote.dto

data class ReservationDto(
    val id: String,
    val hotelId: String,
    val roomId: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,

    val hotel: HotelDto,
    val room:  RoomDto
)