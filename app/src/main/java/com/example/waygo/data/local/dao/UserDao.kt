package com.example.waygo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.waygo.data.local.entity.UserEntity

//TODO: Log.d...
@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity

    @Insert
    fun insertUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM userentity WHERE username = :username")
    suspend fun isUsernameTaken(username: String): Int
}