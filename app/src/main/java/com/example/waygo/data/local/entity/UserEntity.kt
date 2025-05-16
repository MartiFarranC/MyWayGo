package com.example.waygo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class UserEntity(
    @PrimaryKey val id: String,
    var email: String,
    var username: String, //TODO UNIC
    var birthdate: Date,
    var address : String,
    var phone : String,
    var country : String,
    var receiveEmail : Boolean,
)