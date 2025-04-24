package com.example.waygo.entity;

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "itinerary_items")
data class ItineraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int, // Foreign key to associate with a Trip
    val title: String,
    val description: String,
    val date: Date
)