package com.example.waygo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val email: String,
    val username: String,
    val hashedPassword: String
)