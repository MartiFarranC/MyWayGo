package com.example.waygo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDateTime

@Entity
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val destinations: String,
    val participants: String,
    val startDate: String, //TODO: Ha de ser Data
    val endDate: String,//TODO: Ha de ser Data
    val userId: String
)

