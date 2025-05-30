package com.example.waygo.data.remote.dto

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val imageUrl: String,
    val rooms: List<RoomDto>? = null
)

