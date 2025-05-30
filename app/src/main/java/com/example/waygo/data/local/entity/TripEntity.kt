package com.example.waygo.data.local.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val destinations: String,
    val participants: String,
    val startDate: String,
    val endDate: String,
    val userId: String,
    val images: List<Uri>
)

