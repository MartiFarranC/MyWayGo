package com.example.waygo.data.local.entity

import androidx.room.*

@Entity(
    tableName = "image",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class ImageEntity( //TODO: Podem eliminar?
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val uri: String
)