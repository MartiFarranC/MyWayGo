package com.example.waygo.dao

import androidx.room.*
import com.example.waygo.entity.TripEntity

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("SELECT * FROM TripEntity WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity?

    @Query("SELECT * FROM TripEntity ORDER BY startDate ASC")
    suspend fun getAllTrips(): List<TripEntity>
}