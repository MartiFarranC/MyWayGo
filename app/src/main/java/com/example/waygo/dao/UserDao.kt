package com.example.waygo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.waygo.entity.UserEntity

//TODO: Log.d...
@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity

    @Insert
    fun insertUser(user: UserEntity)
}