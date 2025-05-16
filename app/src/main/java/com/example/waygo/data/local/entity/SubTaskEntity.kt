package com.example.waygo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subtasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["parentTaskId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val parentTaskId: Int,
    val title: String,
    val description: String
)