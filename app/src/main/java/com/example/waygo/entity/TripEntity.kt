package com.example.waygo.entity;

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips")
data class TripEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val name: String,
        val destinations: String,
        val participants: String,
        val startDate: Date,
        val endDate: Date
)

