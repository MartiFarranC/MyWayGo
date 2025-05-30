package com.example.waygo.data.local.dao

import androidx.room.*
import com.example.waygo.data.local.entity.ImageEntity
import com.example.waygo.data.local.entity.TripEntity

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrip(trip: TripEntity)

    @Query("SELECT * FROM TripEntity WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity?

    @Query("SELECT * FROM TripEntity ORDER BY startDate ASC")
    suspend fun getAllTrips(): List<TripEntity>

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("SELECT * FROM TripEntity WHERE userId = :userId ORDER BY startDate ASC")
    suspend fun getTripsByUserId(userId: String): List<TripEntity>
}