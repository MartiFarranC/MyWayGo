package com.example.waygo.dao;

import androidx.room.*
import com.example.waygo.entity.TripEntity

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    suspend fun getAllTrips(): List<TripEntity>

    @Insert
    suspend fun insertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)
}