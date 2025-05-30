package com.example.waygo.data.remote.dto

data class RoomDto(
    val id: String,
    val roomType: String,
    val price: Float,
    val images: List<String>
)