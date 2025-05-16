package com.example.waygo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItineraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val description: String,
)