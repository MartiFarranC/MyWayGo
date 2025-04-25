package com.example.waygo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ItineraryItem")
data class ItineraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val description: String,
    val scheduledTime: Int,
    val order: Int
)