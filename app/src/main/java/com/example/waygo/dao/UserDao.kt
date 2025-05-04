package com.example.waygo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.waygo.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
}